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
public class ImagePullTaska extends AbstractOperateTask {
    private final static Logger logger = LoggerFactory.getLogger(ImagePullTaska.class);
    private IAppImageBpo appImageBpo;
    private String command;
    private String command1;
    private String command2;
    private Long id;

    public ImagePullTaska(Long id, String command, String command1, String command2, IAppImageBpo appImageBpo){
        this.command = command;
        this.command1 = command1;
        this.command2 = command2;
        this.id = id;
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
            Runtime.getRuntime().exec(command);
            //pull完成
            appImageBpo.updateImageDownload(id,DownloadState.SAVEIMAGETOIMG);
            Runtime.getRuntime().exec(command1);
            //将文件拉取
            appImageBpo.updateImageDownload(id,DownloadState.SCPFILE);
//            Runtime.getRuntime().exec(command2);
            appImageBpo.updateImageDownload(id,DownloadState.DOWNLOADING);
        }catch (Exception e) {
            appImageBpo.updateImageDownload(id,DownloadState.DOWNLOADFIALD);
            appImageBpo.updateImageDownNum(id,0);
        }
    }

    @Override
    public void successfullyExecuted() {
        appImageBpo.updateImageDownload(id,DownloadState.DOWNLOADING);
    }
}
