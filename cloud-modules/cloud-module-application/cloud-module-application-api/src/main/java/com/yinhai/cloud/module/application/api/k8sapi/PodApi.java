package com.yinhai.cloud.module.application.api.k8sapi;

import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.vo.NamespaceVo;
import com.yinhai.cloud.module.application.api.vo.PodLogVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.util.NodeUtils;
import com.yinhai.core.common.api.util.ValidateUtil;

import java.util.Map;

/**
 * @Author ：kangdw
 * @Date : 2019/4/8
 */
public class PodApi {
    /**
     * 查询某个Pod的日志根据过滤条件，如果过滤条件为Since则按照时间获取，如果为tail则按照行数获取
     *
     * @param vo
     * @param namespaceVo
     * @param node
     * @return
     * @throws SSHExecuteException
     * @throws SSHConnectionException
     */
    public static String getPodLog(PodLogVo vo, NamespaceVo namespaceVo, NodeBasicInfoVo node) throws SSHExecuteException, SSHConnectionException {
        ConnVo connVo = NodeUtils.createConnFromNode(node);
        ConsoleCommand commandVo = new ConsoleCommand();
        StringBuffer commandBuffer = new StringBuffer();
        commandBuffer.append("source ~/.bash_profile && kubectl logs ");

        //如果是按照行查询
        if (vo.getFilterType().equals(IAppConstants.POD_LOG_TAIL)&&!ValidateUtil.isEmpty(vo.getLineNum())) {
            commandBuffer.append("--");
            commandBuffer.append(vo.getFilterType());
            commandBuffer.append("=");
            commandBuffer.append(vo.getLineNum());
        } else if (vo.getFilterType().equals(IAppConstants.POD_LOG_SINCE)&&!ValidateUtil.isEmpty(vo.getInputTimes())) {
            commandBuffer.append("--");
            commandBuffer.append(vo.getFilterType());
            commandBuffer.append("=");
            //如果是按照时间查询
            commandBuffer.append(vo.getInputTimes());
        }
        commandBuffer.append(" ");
        commandBuffer.append(vo.getPodName());
        commandBuffer.append(" -n");
        commandBuffer.append(" ");
        commandBuffer.append(namespaceVo.getNamespaceIdentify());
        commandVo.appendCommand(commandBuffer.toString());
        Map resultMap = CommonSshExecUtil.exec(connVo, commandVo);
        if (!ValidateUtil.isEmpty(resultMap) && resultMap.size() > 0) {
            MsgVO msgVo = (MsgVO) resultMap.values().iterator().next();
            String msg = msgVo.getSysoutMsg();
            if (ValidateUtil.isNotEmpty(msg)) {
                return msg;
            }
        }
        return null;
    }
}