package com.yinhai.cloud.module.monitor.impl.job;

import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.exception.SSHConnectionException;
import com.yinhai.cloud.core.api.exception.SSHExecuteException;
import com.yinhai.cloud.core.api.ssh.command.ConsoleCommand;
import com.yinhai.cloud.core.api.util.CommonSshExecUtil;
import com.yinhai.cloud.core.api.vo.ConnVo;
import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.monitor.api.bpo.IClusterMonitorBpo;
import com.yinhai.cloud.module.monitor.api.bpo.INodeMonitorBpo;
import com.yinhai.cloud.module.monitor.api.vo.ClusterMonitorVo;
import com.yinhai.cloud.module.monitor.api.vo.NodeMonitorVo;
import com.yinhai.cloud.module.monitor.constants.MonitorConstants;
import com.yinhai.cloud.module.resource.cluster.api.bpo.IClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.nodes.api.bpo.INodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.core.app.api.util.ServiceLocator;
import com.yinhai.core.common.api.exception.AppException;
import com.yinhai.modules.schedul.domain.manager.ClusterJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by pengwei on 2018/9/19.
 * <p>
 * 定时监控任务
 */
@DisallowConcurrentExecution
public class MonitorJob extends ClusterJob {

    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private static final long serialVersionUID = 9021668307094109774L;

    private IClusterBpo clusterBpo;
    private INodeMonitorBpo nodeMonitorBpo;
    private IClusterMonitorBpo clusterMonitorBpo;
    private IAppBpo appBpo;
    private INodeBpo nodeBpo;

    private void initJob() {
        if (clusterBpo == null) {
            clusterBpo = (IClusterBpo) ServiceLocator.getService("clusterBpo");
        }
        if (nodeMonitorBpo == null) {
            nodeMonitorBpo = (INodeMonitorBpo) ServiceLocator.getService("nodeMonitorBpo");
        }
        if (clusterMonitorBpo == null) {
            clusterMonitorBpo = (IClusterMonitorBpo) ServiceLocator.getService("clusterMonitorBpo");
        }
        if (appBpo == null) {
            appBpo = (IAppBpo) ServiceLocator.getService("appBpo");
        }
        if (nodeBpo == null) {
            nodeBpo = (INodeBpo) ServiceLocator.getService("nodeBpo");
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //初始化所有需要的bean
        initJob();
        //获取所有集群信息
        List<ClusterVo> clusterVos = clusterBpo.queryAllClusterBasicInfo(1L);
        //采用ssh方式获取集群状态
        ClusterMonitorVo clusterMonitorVo = null;
        Date date = new Date();
        for (ClusterVo clusterVo : clusterVos) {
            clusterMonitorVo = new ClusterMonitorVo();
            clusterMonitorVo.setClusterId(clusterVo.getId());
            clusterMonitorVo.setClusterName(clusterVo.getName());
            clusterMonitorVo.setBiztime(date);

            List<NodeBasicInfoVo> masters = nodeBpo.queryRunningMasterNodesByClusterId(clusterVo.getId());
            List<NodeBasicInfoVo> nodeList = nodeBpo.queryNodesByClusterId(clusterVo.getId());
            //获取集群状态
            if (masters.isEmpty()) {
                clusterMonitorVo.setClusterStatus(MonitorConstants.CLUSTER_STATUS_NOCREATE);
            } else {

                NodeBasicInfoVo master = masters.get(0);
                Integer clusterStatus = getClusterStatus(master);
                System.out.println("获取集群状态……");
                clusterMonitorVo.setClusterStatus(clusterStatus);
                // 修改cluster_info表集群状态信息
                ClusterVo updateClusterState = new ClusterVo();
                updateClusterState.setId(clusterVo.getId());
                updateClusterState.setRunningState(clusterStatus);
                clusterBpo.updateClusterById(updateClusterState);
                // 修改cluster_info表集群状态信息 结束
                List<Map<String, String>> nodeStatusList = new ArrayList<Map<String, String>>();
                if (MonitorConstants.CLUSTER_STATUS_NORMAL.equals(clusterStatus)) {
                    ConnVo masterConnVo = new ConnVo(master.getNodeIP(), master.getNodeSSHPort(), master.getNodeUser(),
                            master.getNodePassword());
                    //通过master节点获取状态
                    ConsoleCommand consoleCommand = new ConsoleCommand();
                    consoleCommand.appendCommand("source ~/.bash_profile", MonitorConstants.NODENAME_AND_STATUS);
                    try {
                        MsgVO msgVO = CommonSshExecUtil.exec(masterConnVo, consoleCommand).get(consoleCommand);
                        if (msgVO.isSuccess()) {
                            String sout = msgVO.getSysoutMsg();
                            String[] nodeStatusStrs = sout.split("\\n");
                            Map<String, String> nodeStatusInfo = null;
                            for (String nodeStr : nodeStatusStrs) {
                                nodeStatusInfo = new HashMap<String, String>();
                                String[] nodeStatus = nodeStr.split(":");
                                nodeStatusInfo.put("hostname", nodeStatus[0]);
                                String statusStr = nodeStatus[1];
                                nodeStatusInfo.put("status", statusStr.contains(",") == true ? statusStr.split(",")[0] : statusStr);

                                nodeStatusList.add(nodeStatusInfo);
                            }
                            System.out.println("获取节点信息状态……");
                        }
                    } catch (SSHExecuteException e) {
                        e.printStackTrace();
                        throw new AppException(e.getMessage());
                    } catch (SSHConnectionException e) {
                        e.printStackTrace();
                        throw new AppException(e.getMessage());
                    }
                }

                NodeMonitorVo nodeMonitorVo = null;
                Double clusterCpuAvail = 0d;
                Double clusterMemAvail = 0d;
                for (NodeBasicInfoVo nodeBasicInfoVo : nodeList) {
                    nodeMonitorVo = new NodeMonitorVo();
                    nodeMonitorVo.setNodeId(nodeBasicInfoVo.getNodeId());
                    nodeMonitorVo.setClusterId(clusterVo.getId());
                    nodeMonitorVo.setBiztime(date);
                    //获取节点的cpu和内存利用率
                    Map<String, String> result = getNodeMonitorInfo(nodeBasicInfoVo);
                    Double nodeCpuAvail = Double.valueOf(decimalFormat.format(Double.valueOf(result.get("cpuAvail"))));
                    Double nodeMemAvail = Double.valueOf(decimalFormat.format(Double.valueOf(result.get("memAvail"))));
                    clusterCpuAvail += nodeCpuAvail;
                    clusterMemAvail += nodeMemAvail;

                    nodeMonitorVo.setCurrCpuUsedAvail(nodeCpuAvail);
                    nodeMonitorVo.setCurrMemUsedAvail(nodeMemAvail);
                    if (nodeStatusList.isEmpty()) {
                        nodeMonitorVo.setNodeStatus(MonitorConstants.CLUSTER_STATUS_ERROR);
                        nodeMonitorVo.setNodeMaxPod(0);
                        nodeMonitorVo.setNodeUsedPod(0);
                    } else {
                        for (Map<String, String> nodeStatus : nodeStatusList) {
                            if (nodeStatus.get("hostname").equals(nodeBasicInfoVo.getNodeName())) {
                                if (nodeStatus.get("status").contains(MonitorConstants.NODE_STATUS_UNREADY)) {
                                    nodeMonitorVo.setNodeStatus(MonitorConstants.CLUSTER_STATUS_ERROR);
                                } else {
                                    nodeMonitorVo.setNodeStatus(MonitorConstants.CLUSTER_STATUS_NORMAL);
                                }
                                break;
                            }
                        }
                    }
                    nodeMonitorVo.setNodeOs(result.get("osname"));
                    nodeMonitorVo.setNodeMaxPod(Integer.valueOf(result.get("maxpods")));
                    nodeMonitorVo.setNodeUsedPod(Integer.valueOf(result.get("usedpods")));

                    nodeMonitorBpo.insertNodeMonitorData(nodeMonitorVo);
                }
                clusterMonitorVo.setCurrCpuUsedAvail(Double.valueOf(decimalFormat.format(clusterCpuAvail / nodeList.size())));
                clusterMonitorVo.setCurrMemUsedAvail(clusterMemAvail);

                clusterMonitorBpo.insertNewMonitorData(clusterMonitorVo);
            }

        }
    }

    /**
     * 获取集群状态（各组件状态都为heathly才为正常）
     *
     * @param master
     * @return
     * @throws AppException
     */
    private Integer getClusterStatus(NodeBasicInfoVo master) throws AppException {
        ConnVo masterConnVo = new ConnVo(master.getNodeIP(), master.getNodeSSHPort(), master.getNodeUser(),
                master.getNodePassword());
        ConsoleCommand consoleCommand = new ConsoleCommand();
        consoleCommand.appendCommand("source ~/.bash_profile", MonitorConstants.GET_CLUSTER_STATUS);
        try {
            MsgVO msgVO = CommonSshExecUtil.exec(masterConnVo, consoleCommand).get(consoleCommand);
            if (msgVO.isSuccess() && "0".equals(msgVO.getSysoutMsg())) {
                return MonitorConstants.CLUSTER_STATUS_NORMAL;
            } else {
                return MonitorConstants.CLUSTER_STATUS_ERROR;
            }
        } catch (SSHExecuteException e) {
            e.printStackTrace();
            throw new AppException(e.getMessage());
        } catch (SSHConnectionException e) {
            e.printStackTrace();
            throw new AppException("连接主机" + master.getNodeIP() + "失败！");
        }
    }

    /**
     * 获取cpu利用率和已使用内存
     *
     * @param nodeBasicInfoVo
     * @return
     * @throws AppException
     */
    private Map getNodeMonitorInfo(NodeBasicInfoVo nodeBasicInfoVo) throws AppException {
        ConnVo masterConnVo = new ConnVo(nodeBasicInfoVo.getNodeIP(), nodeBasicInfoVo.getNodeSSHPort(), nodeBasicInfoVo.getNodeUser(),
                nodeBasicInfoVo.getNodePassword());
        Map<String, String> result = new HashMap<String, String>();
        ConsoleCommand consoleCommand = new ConsoleCommand();
        //consoleCommand.setWithSudo(true);
        consoleCommand.appendCommand("source ~/.bash_profile", MonitorConstants.CPU_AVAILABILITY, MonitorConstants.SERVER_MEMORY_USED);
        try {
            MsgVO msgVO = CommonSshExecUtil.exec(masterConnVo, consoleCommand).get(consoleCommand);
            if (msgVO.isSuccess()) {
                String sout = msgVO.getSysoutMsg();
                String[] cpuAndMemInfo = sout.split("\\n");
                result.put("cpuAvail", cpuAndMemInfo[0]);
                result.put("memAvail", cpuAndMemInfo[1]);

                return result;
            } else {
                throw new AppException("获取信息出错");
            }
        } catch (SSHExecuteException e) {
            e.printStackTrace();
            throw new AppException(e.getMessage());
        } catch (SSHConnectionException e) {
            e.printStackTrace();
            throw new AppException("连接主机" + nodeBasicInfoVo.getNodeIP() + "失败！");
        }
    }

    /**
     * 获取节点kube相关信息
     *
     * @param nodeBasicInfoVo
     * @return
     * @throws AppException
     */
    private Map<String, String> getNodeKubeInfo(NodeBasicInfoVo nodeBasicInfoVo) throws AppException {
        ConnVo masterConnVo = new ConnVo(nodeBasicInfoVo.getNodeIP(), nodeBasicInfoVo.getNodeSSHPort(), nodeBasicInfoVo.getNodeUser(),
                nodeBasicInfoVo.getNodePassword());
        Map<String, String> result = new HashMap<String, String>();
        ConsoleCommand consoleCommand = new ConsoleCommand();
        //consoleCommand.setWithSudo(true);
        consoleCommand.appendCommand("source ~/.bash_profile", String.format(MonitorConstants.NODE_KUBE_INFO, nodeBasicInfoVo.getNodeName(),
                nodeBasicInfoVo.getNodeName(), nodeBasicInfoVo.getNodeName()));
        try {
            MsgVO msgVO = CommonSshExecUtil.exec(masterConnVo, consoleCommand).get(consoleCommand);
            if (msgVO.isSuccess()) {
                String sout = msgVO.getSysoutMsg();
                String[] nodeKubeInfo = sout.split("\\n");
                if (nodeKubeInfo.length == 3) {
                    result.put("osname", nodeKubeInfo[0].trim());
                    result.put("maxpods", nodeKubeInfo[1].trim());
                    result.put("usedpods", nodeKubeInfo[2].trim());
                    return result;
                } else {
                    throw new AppException("获取信息出错");
                }
            } else {
                throw new AppException("获取信息出错");
            }
        } catch (SSHExecuteException e) {
            e.printStackTrace();
            throw new AppException(e.getMessage());
        } catch (SSHConnectionException e) {
            e.printStackTrace();
            throw new AppException("连接主机" + nodeBasicInfoVo.getNodeIP() + "失败！");
        }
    }
}
