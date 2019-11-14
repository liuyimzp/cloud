package com.yinhai.cloud.module.resource.util;

import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.util.CommonUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.core.api.vo.KubeletCommandFactory;
import com.yinhai.cloud.core.api.vo.LinuxCommandFactory;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeInfoVo;
import com.yinhai.core.common.api.util.ValidateUtil;

import java.text.DecimalFormat;

/**
 * Created by pengwei on 2018/5/28.
 */
public class ServerInfoUtils {

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");


    /**
     * 获取节点资源信息
     *
     * @param node 节点基础信息
     * @return
     */
    public static NodeInfoVo getNodePhysicalResources(NodeBasicInfoVo node) {
        ConnVo conn = NodeUtils.createConnFromNode(node);
        ConsoleCommand preCheck = LinuxCommandFactory.serverPreCheck();
        ConsoleCommand prCmd = LinuxCommandFactory.serverPhysicalResources();
        try {
            MsgVO msgVO = CommonSshExecUtil.exec(conn, preCheck, prCmd).get(prCmd);
            if (!msgVO.isSuccess()) {
                NodeInfoVo info = new NodeInfoVo(NodeState.GET_RESOURCE_EXECUTE_CMD_FAILED);
                info.setErrorMsg("获取主机信息失败：" + msgVO.getErrorMsg());
                return info;
            }
            if (ValidateUtil.isEmpty(msgVO.getSysoutMsg())) {
                NodeInfoVo info = new NodeInfoVo(NodeState.GET_RESOURCE_EXECUTE_CMD_FAILED);
                info.setErrorMsg("获取主机信息失败：执行获取命令返回信息为空");
                return info;
            }
            String[] resourceInfo = msgVO.getSysoutMsg().split("\\n");
            if (resourceInfo.length != 9) {
                NodeInfoVo info = new NodeInfoVo(NodeState.GET_RESOURCE_EXECUTE_CMD_FAILED);
                info.setErrorMsg("获取主机信息失败：获取命令返回格式无法解析 " + msgVO.getSysoutMsg());
                return info;
            }
            NodeInfoVo nodeInfoVo = new NodeInfoVo();
            nodeInfoVo.setNodeCpuInfo(resourceInfo[0]);

            nodeInfoVo.setNodeMemoryInfo(decimalFormat.format(Double.valueOf(resourceInfo[1])));
            //新增：增加可用内存统计
            nodeInfoVo.setNodeFreeMemory(decimalFormat.format(Double.valueOf(resourceInfo[6])));
            nodeInfoVo.setNodeDiskInfoTotal(decimalFormat.format(Double.valueOf(resourceInfo[2])));

            nodeInfoVo.setNodeSystem(resourceInfo[3]);
            nodeInfoVo.setNodeDiskInfoUsable(decimalFormat.format(Double.valueOf(resourceInfo[4])));
            nodeInfoVo.setNodeDiskInfoUsage(decimalFormat.format(Double.valueOf(resourceInfo[5])));

            //var目录空间大小
            nodeInfoVo.setNodeVarDisInfoUsable(decimalFormat.format(Double.valueOf(resourceInfo[7])));
            nodeInfoVo.setNodeVarDisInfoAlwas(decimalFormat.format(Double.valueOf(resourceInfo[8])));

            return nodeInfoVo;


        } catch (SSHExecuteException e) {
            NodeInfoVo info = new NodeInfoVo(NodeState.GET_RESOURCE_EXECUTE_CMD_FAILED);
            String exceptionMsgContent = CommonUtil.getExceptionMsgContent(e);
            info.setErrorMsg(exceptionMsgContent);

            return info;
        } catch (SSHConnectionException e) {
            NodeInfoVo info = new NodeInfoVo(NodeState.GET_RESOURCE_CONNECTION_FAILED);
            info.setErrorMsg("连接主机失败，（请检查IP、用户名、密码、SSH端口）");
            return info;
        }

    }

    /**
     * 获取K8s节点描述信息
     */
    public static NodeInfoVo getK8sNodeDesc(NodeBasicInfoVo masterNode, String childNodeName) {
        ConnVo conn = NodeUtils.createConnFromNode(masterNode);
        ConsoleCommand nodeDescCommand = KubeletCommandFactory.makeK8sNodeAllocatableCmd(childNodeName);
        NodeInfoVo nodeInfoVo = new NodeInfoVo();

        try {
            MsgVO msgVO = CommonSshExecUtil.exec(conn, nodeDescCommand).get(nodeDescCommand);
            if (msgVO.isSuccess()) {
                String infoStr = msgVO.getSysoutMsg();
                String[] infos = infoStr.split("\\n");
                if (infos.length != 2) {
                    NodeInfoVo info = new NodeInfoVo(NodeState.GET_RESOURCE_EXECUTE_CMD_FAILED);
                    info.setErrorMsg("获取主机信息失败：获取命令返回格式无法解析 " + msgVO.getSysoutMsg());
                    return info;
                }
                if (infos[0].endsWith("m")) {
                    nodeInfoVo.setNodeAllocatableCpu(Double.parseDouble(infos[0].substring(0, infos[0].indexOf('m')))/1000);
                } else {
                    nodeInfoVo.setNodeAllocatableCpu(Double.parseDouble(infos[0]));
                }
                nodeInfoVo.setNodeAllocatableMemory(Double.parseDouble(infos[1]));
            }
        } catch (SSHExecuteException e) {
            NodeInfoVo info = new NodeInfoVo(NodeState.GET_RESOURCE_EXECUTE_CMD_FAILED);
            String exceptionMsgContent = CommonUtil.getExceptionMsgContent(e);
            info.setErrorMsg(exceptionMsgContent);
            return info;
        } catch (SSHConnectionException e) {
            NodeInfoVo info = new NodeInfoVo(NodeState.GET_RESOURCE_CONNECTION_FAILED);
            info.setErrorMsg("连接主机失败，（请检查IP、用户名、密码、SSH端口）");
            return info;
        }
        return nodeInfoVo;

    }


}
