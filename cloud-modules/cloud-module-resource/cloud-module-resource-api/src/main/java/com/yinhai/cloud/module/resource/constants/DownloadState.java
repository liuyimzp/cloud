package com.yinhai.cloud.module.resource.constants;

/**
 * Created by liuyi02
 */
public class DownloadState {
    public static final Integer NODOWNLOAD = 0;//0：等待中
    public static final Integer PULLIMAGE = 1;//1：pull镜像
    public static final Integer SAVEIMAGETOIMG = 2;//2：将镜像存为文件
    public static final Integer SCPFILE = 3;//3：将文件拉到服务器
    public static final Integer DOWNLOADING = 4;//4：下载中
    public static final Integer DOWNLOADFIALD = 5;//5：下载失败
}
