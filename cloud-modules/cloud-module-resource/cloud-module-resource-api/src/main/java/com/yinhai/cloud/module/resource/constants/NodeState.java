package com.yinhai.cloud.module.resource.constants;

/**
 * @author zhaokai
 * 节点的操作状态
 */
public interface NodeState {

    // 初始化状态
    Integer INIT_FAILED = -3;
    Integer INIT_RUNNING = 0;
    Integer INIT_DONE = 10;

    // 操作状态

    Integer OPERATE_RUNNING_INIT = 1;
    Integer OPERATE_INIT_DONE = 2;
    Integer OPEATE_NONE = 0;

    Integer OPERATE_STARTING = 11;
    Integer OPERATE_STARTED = 12;

    Integer OPERATE_STOPPING = 13;
    Integer OPERATE_STOPPED = 14;

    Integer OPERATE_DELETING = 15;


    Integer OPERATE_FAILED = -1;


    Integer GET_RESOURCE_CONNECTION_FAILED = -2;
    Integer GET_RESOURCE_EXECUTE_CMD_FAILED = -1;
    Integer GET_RESOURCE_SUCCESSFULLY = 0;

    // 运行状态
    Integer RUNNING_DEPLOY = 0;//初始化
    Integer RUNNING_SUCCESS = 1;//正在运行
    Integer RUNNING_STOP = 3;//停止
    Integer RUNNING_ERROR = 4;//失败
    Integer RUNNING_NOT_NOW = 2;//删除中

}
