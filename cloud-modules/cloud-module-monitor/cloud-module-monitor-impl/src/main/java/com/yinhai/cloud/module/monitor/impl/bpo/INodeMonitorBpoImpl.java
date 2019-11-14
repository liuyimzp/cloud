package com.yinhai.cloud.module.monitor.impl.bpo;

import com.yinhai.cloud.module.monitor.api.bpo.INodeMonitorBpo;
import com.yinhai.cloud.module.monitor.api.vo.NodeMonitorVo;
import com.yinhai.cloud.module.monitor.impl.dao.INodeMonitorDao;
import com.yinhai.cloud.module.monitor.impl.po.NodeMonitorPo;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeBasicInfoPo;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by pengwei on 2018/9/21.
 */
public class INodeMonitorBpoImpl extends TaBaseBpo implements INodeMonitorBpo {

    @Autowired
    private INodeDao nodeDao;

    @Autowired
    private INodeMonitorDao nodeMonitorDao;

    @Override
    public List<NodeMonitorVo> getNodeMonitorInfoByClusterId(Long clusterId) {
        List<NodeMonitorVo> nodeMonitorVoList = new ArrayList<NodeMonitorVo>();
        List<NodeBasicInfoPo> nodeList = nodeDao.queryNodesByClusterId(clusterId);
        if (!nodeList.isEmpty()) {
            NodeMonitorVo nodeMonitorVo = null;
            for (NodeBasicInfoPo nodeBasicInfoPo : nodeList) {
                nodeMonitorVo = new NodeMonitorVo();
                nodeMonitorVo.setNodeId(nodeBasicInfoPo.getNodeId());
                nodeMonitorVo.setClusterId(nodeBasicInfoPo.getClusterId());
                nodeMonitorVo.setNodeIp(nodeBasicInfoPo.getNodeIP());
                nodeMonitorVo.setNodeHostName(nodeBasicInfoPo.getNodeName());
                List<NodeMonitorPo> nodeMonitorPoList = nodeMonitorDao.quaryNodeMonitorDataByNodeId(nodeBasicInfoPo.getNodeId());
                if (!nodeMonitorPoList.isEmpty()) {
                    nodeMonitorVo.setNodeStatus(nodeMonitorPoList.get(0).getNodeStatus());
                    nodeMonitorVo.setNodeOs(nodeMonitorPoList.get(0).getNodeOs());
                    nodeMonitorVo.setNodeMaxPod(nodeMonitorPoList.get(0).getNodeMaxPod());
                    nodeMonitorVo.setNodeUsedPod(nodeMonitorPoList.get(0).getNodeUsedPod());
                } else {
                    continue;
                }

                List<Map> cpuAvails = new ArrayList<Map>();
                List<Map> memAvails = new ArrayList<Map>();
                Map cpuAvail = null;
                Map memAvail = null;
                for (NodeMonitorPo nodeMonitorPo : nodeMonitorPoList) {
                    cpuAvail = new HashMap();
                    memAvail = new HashMap();

                    cpuAvail.put("cpuAvail", nodeMonitorPo.getNodeCPUAvailability());
                    cpuAvail.put("biztime", new SimpleDateFormat("HH:mm:ss").format(nodeMonitorPo.getBiztime()));

                    memAvail.put("memAvail", nodeMonitorPo.getNodeMemoryAvailability());
                    memAvail.put("biztime", new SimpleDateFormat("HH:mm:ss").format(nodeMonitorPo.getBiztime()));

                    cpuAvails.add(cpuAvail);
                    memAvails.add(memAvail);
                }
                nodeMonitorVo.setCpuAvails(cpuAvails);
                nodeMonitorVo.setMemAvails(memAvails);

                nodeMonitorVoList.add(nodeMonitorVo);
            }
        }

        return nodeMonitorVoList;
    }

    @Override
    public void insertNodeMonitorData(NodeMonitorVo nodeMonitorVo) {
        NodeMonitorPo po = new NodeMonitorPo();
        po.setNodeId(nodeMonitorVo.getNodeId());
        po.setClusterId(nodeMonitorVo.getClusterId());
        po.setNodeStatus(nodeMonitorVo.getNodeStatus());
        po.setNodeOs(nodeMonitorVo.getNodeOs());
        po.setNodeMaxPod(nodeMonitorVo.getNodeMaxPod());
        po.setNodeUsedPod(nodeMonitorVo.getNodeUsedPod());
        po.setNodeCPUAvailability(nodeMonitorVo.getCurrCpuUsedAvail());
        po.setNodeMemoryAvailability(nodeMonitorVo.getCurrMemUsedAvail());
        po.setBiztime(nodeMonitorVo.getBiztime());

        nodeMonitorDao.insertNodeMonitorData(po);
    }

    @Override
    public void deleteNodeMonitorData(Long nodeId) {

        nodeMonitorDao.deleteNodeMonitorData(nodeId);
    }


    public void deleteExpireMonitorData(Date biztime) {
        return;
    }
}
