package com.yinhai.cloud.module.application.api.util;


import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liuyi02 on 2019/09/19
 */

public class MiddlewareConfigUtil {

    private final static Logger logger = LoggerFactory.getLogger(MiddlewareConfigUtil.class);

    /**
     * 将中间件的配置文件写入对应管理节点，且设置改中间件只能部署到该节点上
     * @param application app详细信息
     * @param masterNode 管理节点
     * @param configStr 对应中间件配置文件path
     */
    public static void setConfigMiddleware(AppVo application, NodeBasicInfoVo masterNode,String configStr){
        try {
            ConnVo connVo = NodeUtils.createConnFromNode(masterNode);
            ConsoleCommand command = new ConsoleCommand();
            command.appendCommand("mkdir -p " + configStr + " && echo \"" + application.getMiddleware_configfile() + "\"> " + configStr + "/" + application.getAppIdentify() + ".conf");
            CommonSshExecUtil.exec(connVo,command);
        }catch (Exception e){
            logger.error(logger.getName() + "content",e);
        }
    }
}