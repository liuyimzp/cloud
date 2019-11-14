package com.yinhai.cloud.module.repertory.api.util;

import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.ssh.command.DownloadFileCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import com.yinhai.cloud.module.resource.constants.DownloadState;
import com.yinhai.cloud.module.resource.task.api.AbstractOperateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: liuyi02
 */
public class ImagePullTask extends AbstractOperateTask {
    private final static Logger logger = LoggerFactory.getLogger(ImagePullTask.class);
    private IAppImageBpo appImageBpo;
    private ConsoleCommand command;
    private ConsoleCommand command1;
    private ConsoleCommand command2;
    private Long id;
    private DownloadFileCommand command3;
    private ConnVo dockerRepoServerConn;

    public ImagePullTask(Long id,ConsoleCommand command,ConsoleCommand command1,ConsoleCommand command2, DownloadFileCommand command3,ConnVo dockerRepoServerConn,IAppImageBpo appImageBpo){
        this.command = command;
        this.command1 = command1;
        this.command2 = command2;
        this.command3 = command3;
        this.id = id;
        this.dockerRepoServerConn = dockerRepoServerConn;
        this.appImageBpo = appImageBpo;
    }

    @Override
    public boolean beforeExecute() {
        appImageBpo.updateImageDownload(id,DownloadState.NODOWNLOAD);
        appImageBpo.updateImageDownNum(id,1);
        return true;
    }

    @Override
    public boolean needCheck() {
        return false;
    }

    @Override
    public void executeFailed(Exception e) {
        appImageBpo.updateImageDownload(id,DownloadState.DOWNLOADFIALD);
        //将下载人数置为零
        appImageBpo.updateImageDownNum(id,0);
    }

    @Override
    public void onExecute() throws Exception {
        try{
            appImageBpo.updateImageDownload(id,DownloadState.PULLIMAGE);
            //pull镜像
            CommonSshExecUtil.exec(dockerRepoServerConn,command);
            //pull完成
            appImageBpo.updateImageDownload(id,DownloadState.SAVEIMAGETOIMG);
            CommonSshExecUtil.exec(dockerRepoServerConn,command1);
            //将文件拉取
            appImageBpo.updateImageDownload(id,DownloadState.SCPFILE);
            CommonSshExecUtil.exec(dockerRepoServerConn,command3,command2);
            appImageBpo.updateImageDownload(id,DownloadState.DOWNLOADING);
        }catch (SSHConnectionException e) {
            appImageBpo.updateImageDownload(id,DownloadState.DOWNLOADFIALD);
            appImageBpo.updateImageDownNum(id,0);
        }
    }

    @Override
    public void successfullyExecuted() {
        appImageBpo.updateImageDownload(id,DownloadState.DOWNLOADING);
    }
}
