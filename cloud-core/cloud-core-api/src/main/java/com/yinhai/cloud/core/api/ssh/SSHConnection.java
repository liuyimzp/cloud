package com.yinhai.cloud.core.api.ssh;

import com.jcraft.jsch.*;
import com.yinhai.cloud.core.api.constant.SshKey;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.exception.SSHFileTransferException;
import com.yinhai.cloud.core.api.handler.SSHExecRunningHandler;
import com.yinhai.cloud.core.api.ssh.command.AbstractCommand;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.vo.ConnVo;
import net.neoremind.sshxcute.core.SysConfigOption;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;

/**
 * @author: zhaokai
 * @create: 2018-08-16 10:18:12
 */
public class SSHConnection {
    private final static Logger logger = LoggerFactory.getLogger(SSHConnection.class);

    private Session session;
    private Long taskExecuteTimeInterval = SshKey.TimeInterval;
    private Channel channel;
    private ChannelSftp sftpChannel;
    private String ip;
    private String username;
    private String password;
    private Integer port = SshKey.sshPort;
    private SessionWrapper sessionWrapper;
    private static final String REGEX = "(\\u001B\\[\\d+m)|(\\u001B\\[\\d+;\\d+m)";//过滤颜色乱码正则表达式

    public SSHConnection(ConnVo connInfo) {
        this.ip = connInfo.getIp();
        this.username = connInfo.getUser();
        this.password = connInfo.getPwd();
        if (connInfo.getPort() != null) {
            this.port = connInfo.getPort();
        }

    }

    public void setTaskExecuteTimeInterval(Long taskExecuteTimeInterval) {
        this.taskExecuteTimeInterval = taskExecuteTimeInterval;
    }

    private void checkParam() throws SSHConnectionException {

        if (ip == null) {
            throw new SSHConnectionException("connection info host ip  can't be empty!");
        }
        if (username == null) {
            throw new SSHConnectionException("connection info username  can't be empty!");
        }
        if (password == null) {
            throw new SSHConnectionException("connection info password  can't be empty!");
        }

    }

    public void openConnection() throws SSHConnectionException {
        checkParam();
        sessionWrapper = SSHSessionPool.dispatchSessionForCurrentThread(ip, username, password, port);

        session = sessionWrapper.getSession();
    }

    private void checkOpened() {
        if (session == null || !session.isConnected()) {
            throw new IllegalStateException("Haven't open ssh session yet");
        }

    }

    public Boolean isConnected() {
        return session.isConnected();
    }

    public Boolean closeConnection() {
        try {
            if (sessionWrapper != null) {
                sessionWrapper.setThreadId(null);
                session = null;
            }
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
                channel = null;
            }


            if (sftpChannel != null && sftpChannel.isConnected()) {
                sftpChannel.disconnect();
                sftpChannel = null;
            }
            logger.info("SSH connection released!");
        } catch (Exception e) {
            logger.error("SSH connection release  failed with the following exception: " + e);
            return false;
        }
        return true;
    }


    public synchronized ExecResult exec(AbstractCommand cmd, SSHExecRunningHandler handler) throws SSHExecuteException {
        checkOpened();
        ExecResult result = new ExecResult();

        BufferedReader info = null;
        try {
            channel = session.openChannel("exec");
            String displayCommand = cmd.buildRunCmd();
            String command = displayCommand;
            if (cmd.isWithSudo()) {
                command = cmd.buildRunCmdWithSudo(this.password);
            }
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            channel.setOutputStream(System.out);
            FileOutputStream fos = new FileOutputStream(SysConfigOption.ERROR_MSG_BUFFER_TEMP_FILE_PATH);
            ((ChannelExec) channel).setErrStream(fos);
            channel.connect();
            info = new BufferedReader(new InputStreamReader(channel.getInputStream(), SshKey.CODE_FORMAT_UTF8));
            logger.info("Session["+session+"] Start to run command at "+ip+" : " + displayCommand);
            StringBuilder sb = new StringBuilder();
            String line;
            while (true) {
                while ((line = info.readLine()) != null) {
                    sb.append(line.replaceAll(REGEX,"")).append("\n");
                    if (handler != null) {
                        handler.handle(cmd, line);
                    }
                }
                if (channel.isClosed()) {
                    if (cmd.checkSuccess(sb.toString(), channel.getExitStatus())) {
                        logger.info("Execute successfully for command");
                        result.setSystemOut(sb.toString());
                        result.setErrorOut("");
                        result.setSuccess(true);
                    } else {
                        result.setSystemOut(sb.toString());
                        result.setErrorOut(readStringFormFile(SysConfigOption.ERROR_MSG_BUFFER_TEMP_FILE_PATH));
                        result.setSuccess(false);
                        logger.error("Execution failed while executing command: " + displayCommand);
                        logger.error("Error message: " + result.getErrorOut());
                        if (SysConfigOption.HALT_ON_FAILURE) {
                            logger.info("The task has failed to execute :" + cmd.buildRunCmd() + ". So program exit.");
                            break;
                        }
                    }
                    break;
                }
            }
            try {
                this.wait(taskExecuteTimeInterval);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        } catch (JSchException | IOException e) {
            logger.error(e.getMessage());
            throw new SSHExecuteException(e);
        } finally {
            if (info != null) {
                try {
                    info.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }

            if (channel != null) {
                channel.disconnect();
                logger.info("Connection channel disconnect");
            }

        }
        return result;
    }


    private void openSftpChannel() throws SSHFileTransferException {
        try {
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
        } catch (JSchException e) {
            throw new SSHFileTransferException(e.getMessage());
        }
    }


    public synchronized void uploadFileToServer(InputStream is, String remoteFileAbsolutePath) throws SSHFileTransferException {
        checkOpened();
        openSftpChannel();
        SlashPath filePath = new SlashPath(remoteFileAbsolutePath);
        String parentAbsolutePath = makeRemoteDirs(filePath.getParentPath());
        String remoteFullPath = new SlashPath(parentAbsolutePath, filePath.getName()).getFullPath();
        try {
            sftpChannel.put(is, remoteFullPath);
        } catch (SftpException e) {
            throw new SSHFileTransferException(e);
        } finally {
            closeSftpChannel();
        }


    }

    private void closeSftpChannel() {
        if (sftpChannel != null) {
            sftpChannel.disconnect();
            sftpChannel = null;
        }
    }

    /**
     * 上传文件到服务器
     *
     * @param localFilePath  本地文件绝对路径
     * @param remoteFileName 远端文件绝对路径
     * @throws SSHFileTransferException
     */
    public synchronized void uploadFileToServer(String localFilePath, String remoteFileName) throws SSHFileTransferException {
        checkOpened();
        openSftpChannel();
        SlashPath localPath = new SlashPath(localFilePath);
        SlashPath remotePath = new SlashPath(remoteFileName);
        String parentAbsolutePath = makeRemoteDirs(remotePath.getParentPath());
        String remoteFullPath = new SlashPath(parentAbsolutePath, remotePath.getName()).getFullPath();
        try {
            sftpChannel.put(localPath.getFullPath(), remoteFullPath);
        } catch (SftpException e) {
            throw new SSHFileTransferException(e);
        } finally {
            closeSftpChannel();
        }


    }


    private String readStringFormFile(String filename) {
        try(InputStream is = new FileInputStream(filename)) {
           return  IOUtils.toString(is,"UTF-8");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return "";

    }

    /**
     * 下载远端文件到本地
     *
     * @param remoteFileAbsolutePath 远端文件绝对路径
     * @param localFileAbsolutePath  本地文件绝对路径
     * @throws SSHFileTransferException
     */
    public synchronized void downloadFileFromServer(String remoteFileAbsolutePath, String localFileAbsolutePath) throws SSHFileTransferException {
        openSftpChannel();
        File parentFile = new File(localFileAbsolutePath).getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            throw new SSHFileTransferException("创建本地文件夹失败" + parentFile);
        }
        try {
            sftpChannel.get(remoteFileAbsolutePath, localFileAbsolutePath);
        } catch (SftpException e) {
            throw new SSHFileTransferException(e);
        } finally {
            closeSftpChannel();

        }

    }


    /**
     * 将本地文件夹下的所有内容上传到服务器
     *
     * @param localDir        需要是已经存在的文件夹
     * @param remoteParentDir 该路径为绝对路径，没有的话会自动创建 <br/>
     *                        该文件夹做为上传的父目录如将本地/user/abc 上传到服务器/opt/efg <br/>
     *                        则最终服务器上呈现的是/opt/efg/abc 目录
     * @throws SSHFileTransferException
     */
    public synchronized void uploadLocalDirToServer(String localDir, String remoteParentDir) throws SSHFileTransferException {
        checkOpened();
        openSftpChannel();
        try {
            File localDirFile = new File(localDir);
            if (!localDirFile.exists()) {
                throw new SSHFileTransferException("local directory doesn't exist!");
            }
            if (!localDirFile.isDirectory()) {
                throw new SSHFileTransferException(localDir + " is a file!");
            }
            SlashPath remoteBaseDirPath = new SlashPath(remoteParentDir, localDirFile.getName());
            // 创建顶层目录
            makeRemoteDirs(remoteBaseDirPath.getFullPath());
            Collection<File> files = FileUtils.listFiles(localDirFile, FileFilterUtils.trueFileFilter(), FileFilterUtils.trueFileFilter());

            for (File f : files) {
                String localDirPath = localDirFile.getAbsolutePath();
                String currentFileParentPath = f.getParentFile().getAbsolutePath();
                // 截取出当前文件的父文件夹路径
                String subParent = currentFileParentPath.substring(localDirPath.length());
                try {
                    SlashPath rp = new SlashPath(remoteBaseDirPath, subParent);
                    // 重新将路径构建为/ 开头的绝对路径
                    String rpAbsolute = makeRemoteDirs(rp.getFullPath());
                    String remoteFullPath = new SlashPath(rpAbsolute, f.getName()).getFullPath();
                    sftpChannel.put(f.getAbsolutePath(),remoteFullPath );
                } catch (SftpException e1) {
                    throw new SSHFileTransferException(e1);
                }
            }
        } finally {
            closeSftpChannel();
        }

    }


    /**
     * 连续的创建不存在的文件夹
     *
     * @param remoteParentDir 文件夹路径
     * @throws SSHFileTransferException
     */
    private String makeRemoteDirs(String remoteParentDir) throws SSHFileTransferException {
        ConsoleCommand mkdir = new ConsoleCommand();
        mkdir.appendCommand("mkdir -p "+remoteParentDir).appendCommand("cd "+remoteParentDir).appendCommand("pwd");
        try {
            ExecResult result = exec(mkdir, null);
            if(result.isSuccess()){
                return result.getSystemOut().trim();
            }
            throw new SSHFileTransferException("make dir "+remoteParentDir+" error "+result.getErrorOut());
        } catch (SSHExecuteException e) {
            throw new SSHFileTransferException("make dir "+remoteParentDir+" error "+e);
        }
    }


    public synchronized void downloadFileFromServer(String remoteFilePath, OutputStream out) throws SSHFileTransferException {
        checkOpened();
        openSftpChannel();
        try {
            sftpChannel.get(remoteFilePath, out);
        } catch (SftpException e) {
            throw new SSHFileTransferException(e);
        }finally {
            closeSftpChannel();
        }

    }



    /**
     * 下载文件夹，非sudo查看文件列表方式
     *
     * @param remoteDir
     * @param localDir
     * @throws SSHFileTransferException
     */
    public synchronized void downloadDir(String remoteDir, String localDir) throws SSHFileTransferException {
        downloadDir(remoteDir, localDir, false);
    }

    /**
     * 下载文件夹
     *
     * @param remoteDir 远端文件夹绝对路径
     * @param localDir 本地文件夹，作为父目录
     * @param withSudo 是否需要sudo权限进行下载
     * @throws SSHFileTransferException
     */
    public synchronized void downloadDir(String remoteDir, String localDir, Boolean withSudo) throws SSHFileTransferException {
        checkOpened();
        openSftpChannel();
        ChannelExec cmdChannel = null;
        BufferedReader info = null;
        String displayCmd = "ls -lR " + remoteDir;
        String lsCmd = displayCmd;
        if (withSudo != null && withSudo) {
            lsCmd = "echo '" + password + "' |sudo -S ls -lR " + remoteDir;
        }

        try {
            cmdChannel = (ChannelExec) session.openChannel("exec");
            cmdChannel.setCommand(lsCmd);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            cmdChannel.setErrStream(out);
            cmdChannel.connect();
            info = new BufferedReader(new InputStreamReader(cmdChannel.getInputStream(), "UTF-8"));
            String line;
            String currentSubDirPath = "";
            StringBuilder infoSb = new StringBuilder();
            while ((line = info.readLine()) != null) {
                infoSb.append(line).append("\n");
                boolean isMainDir = (remoteDir + "/:").equals(line) || (remoteDir + ":").equals(line);
                boolean lineIsSubDir = line.startsWith(remoteDir) && line.endsWith(":") && !isMainDir;
                if (isMainDir) {
                    File dir = new File(localDir);
                    if (!dir.exists() && !dir.mkdirs()) {
                        throw new SSHFileTransferException("创建文件夹失败：" + dir);
                    }

                } else if (lineIsSubDir) {
                    // 文件夹
                    String subDirPath = line.substring(remoteDir.length(), line.lastIndexOf(':'));
                    if (subDirPath.startsWith(SlashPath.PATH_SEPARATOR)) {
                        subDirPath = subDirPath.substring(1);
                    }
                    File dir = new File(localDir, subDirPath);
                    if (!dir.exists() && !dir.mkdirs()) {
                        throw new SSHFileTransferException("创建文件夹失败：" + dir);
                    }
                    currentSubDirPath = subDirPath;
                } else if (line.startsWith("-")) {
                    // 文件
                    String[] arr = line.split("\\s+");
                    String fileName = arr[arr.length - 1];
                    String remoteFilePath = remoteDir + SlashPath.PATH_SEPARATOR + currentSubDirPath + SlashPath.PATH_SEPARATOR + fileName;
                    if ("".equals(currentSubDirPath)) {
                        remoteFilePath = remoteDir + SlashPath.PATH_SEPARATOR + fileName;
                    }
                    this.sftpChannel.get(remoteFilePath, localDir + SlashPath.PATH_SEPARATOR + currentSubDirPath);
                    logger.info(" successfully downloaded server " + ip + " file " + remoteFilePath + " to local dir " + localDir);
                }

            }
            if (0 != cmdChannel.getExitStatus()) {
                byte[] bytes = out.toByteArray();
                throw new SSHFileTransferException("执行命令 " + displayCmd + " 失败:" + new String(bytes, "UTF-8") + "\n" + infoSb);

            }
        } catch (JSchException | IOException | SftpException e) {
            throw new SSHFileTransferException(e);
        } finally {
            if (info != null) {
                try {
                    info.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }

            }
            if (cmdChannel != null) {
                cmdChannel.disconnect();
            }
            closeSftpChannel();
        }
    }

    public String removeLuanma(String str){
        String resultStr = str.replace("\u001B[0;39m","").replace("\u001B[2m","").replace("\u001B[36m","").replace("\u001B[32m","").replace("\u001B[35m","");
        return resultStr;
    }

}
