package com.yinhai.cloud.module.repertory.api.util;

import com.yinhai.cloud.core.api.constant.ConfigPropKey;
import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.exec.ShellUid;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.ShellCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppRepertoryBpo;
import com.yinhai.cloud.module.repertory.api.constant.RepositoryConstant;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.cloud.module.repertory.api.vo.AppRepertoryVo;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.cloud.module.shell.util.ShellExecUtil;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author: zhaokai
 * @create: 2018-08-02 11:32:01
 */
public class BuildJavaApplicationDockerImageTool {
    private final static Logger LOGGER = LoggerFactory.getLogger(BuildJavaApplicationDockerImageTool.class);
    private static INodeBpo nodeBpo = ServiceLocator.getService(INodeBpo.class);
    private static IAppRepertoryBpo appRepertoryBpo = ServiceLocator.getService(IAppRepertoryBpo.class);
    private static IAppImageBpo appImageBpo = ServiceLocator.getService(IAppImageBpo.class);

    /**
     * 在指定节点进行自定义镜像制作
     *
     * @param buildImage
     */
    public static String build(AppImageVo buildImage) throws SSHExecuteException, SSHConnectionException {

        AppRepertoryVo imageRepository = appRepertoryBpo.getAppRepertory(buildImage.getRepertoryId());
        // docker 私有仓库信息
        String privateDockerRepo = SystemConfigUtil.getSystemConfigValue("docker.private.repo.ip") + ":" + SystemConfigUtil.getSystemConfigValue("docker.private.repo.port");
        // 拉取tomcat 、jdk等资源包文件服务器地址
        String yumPrivateRepo = SystemConfigUtil.getSystemConfigValue("yum.private.repo");

        String dockerImageTag = imageRepository.getGroupIdentify() + "/" + imageRepository.getIdentify() + ":" + buildImage.getVersion();
        String osVersion = buildImage.getDiyOperateSystem();
        String jdkVersion = buildImage.getDiyJdkVersion();
        String webContainer = buildImage.getDiyWebContainer();
        String applicationFileName = buildImage.getLocalFileName();
        String shellUid = ShellUid.BUILD_DIY_JAVA_DOCKER_IMAGE;
        // 从Home目录开始
        String buildMainDirPath = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.DOCKER_IMAGE_BUILD_MAIN_DIR_PATH);
        String shellRunDirPath = buildMainDirPath + ServerCmdConstant.FILE_SEPARATOR + buildImage.getImageBuildDirName();

        // 构造脚本参数
        BuildImageShellParam shellParams = new BuildImageShellParam(privateDockerRepo, yumPrivateRepo, dockerImageTag, osVersion, jdkVersion, applicationFileName);
        if (RepositoryConstant.WAR_FILE_SUFFIX.equals(buildImage.getDiyType())) {
            shellParams.webContainerVersion = webContainer;
        }
        // 放置应用程序文件的目录路径
        String applicationDir = SystemConfigUtil.getSystemConfigValue(ConfigPropKey.DOCKER_IMAGE_BUILD_MAIN_DIR_PATH) + "/"
                + RepositoryConstant.DOCKER_APPLICATION_UPLOAD_SERVER_DIR_NAME;
        // 重命名应用文件
        final ConsoleCommand renameApplicationName = new ConsoleCommand();
        renameApplicationName.appendCommand("mv -f " + applicationDir + "/" + buildImage.getUploadTmpFileName() + " " + applicationDir + "/" + buildImage.getLocalFileName());

        // 创建Build的目录
        final ConsoleCommand makeShellRunDir = new ConsoleCommand();
        makeShellRunDir.appendCommand("mkdir -p " + shellRunDirPath);
        ConsoleCommand shellRun = new ConsoleCommand();
        shellRun.appendCommand();

        // 脚本执行命令
        final ShellCommand shellCommand = new ShellCommand();

        shellCommand.setShellContent(ShellExecUtil.getShell(shellUid));
        final String shellName = CodeTableUtil.getDesc(ShellUid.SHELL_UID, shellUid);
        shellCommand.setShellName(shellName);
        shellCommand.appendShellParam(shellParams.toString());
        shellCommand.setShellServerWorkDir(shellRunDirPath);

        MsgVO result = CommonSshExecUtil.exec(NodeUtils.createDockerRepoServerConn(),
                renameApplicationName,
                makeShellRunDir,
                shellCommand
        )
                .get(shellCommand);
        return result.getSysoutMsg();
    }

    private static class BuildImageShellParam {
        private String privateDockerRepository;
        private String yumFileServerIp;
        private String dockerImageTag;
        private String operateSystemVersion;
        private String jdkVersion;
        private String applicationFileName;
        private String webContainerVersion;

        public BuildImageShellParam(String privateDockerRepository, String yumFileServerIp, String dockerImageTag, String operateSystemVersion, String jdkVersion, String applicationFileName) {
            this.privateDockerRepository = privateDockerRepository;
            this.yumFileServerIp = yumFileServerIp;
            this.dockerImageTag = dockerImageTag;
            this.operateSystemVersion = operateSystemVersion;
            this.jdkVersion = jdkVersion;
            this.applicationFileName = applicationFileName;
        }


        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(" ").append(this.privateDockerRepository)
                    .append(" ").append(this.yumFileServerIp)
                    .append(" ").append(this.dockerImageTag)
                    .append(" ").append(this.operateSystemVersion)
                    .append(" ").append(this.jdkVersion)
                    .append(" ").append(this.applicationFileName)
                    .append(" ").append(this.webContainerVersion);
            return sb.toString();
        }
    }

}
