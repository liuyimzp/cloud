package com.yinhai.cloud.module.resource.task;

import com.yinhai.cloud.module.resource.cluster.api.bpo.ICephClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.ICephPoolBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.CephPoolVo;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.ICephNodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.CephNodeInfoVo;
import com.yinhai.cloud.module.resource.task.api.AbstractOperateTask;
import com.yinhai.cloud.module.resource.util.ServerUtils;
import com.yinhai.core.app.api.util.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: liuyi02
 */
public class CephClusterDeleteTask extends AbstractOperateTask {
    private final static Logger logger = LoggerFactory.getLogger(CephClusterDeleteTask.class);
    private Long clusterId;
    private List<CephNodeInfoVo> nodes,osdNodes,monNodes;//集群列表信息
    private CephNodeInfoVo monNode = new CephNodeInfoVo();
    protected ICephClusterBpo cephClusterBpo = ServiceLocator.getService(ICephClusterBpo.class);
    protected ICephNodeBpo cephNodeBpo = ServiceLocator.getService(ICephNodeBpo.class);
    protected ICephPoolBpo cephPoolBpo = ServiceLocator.getService(ICephPoolBpo.class);

    public CephClusterDeleteTask(Long clusterId) {
        this.clusterId = clusterId;
    }

    @Override
    public boolean beforeExecute() {
        nodes = cephNodeBpo.getAllCephNode(clusterId);
        osdNodes = nodes.stream().filter(fvo -> ServerCmdConstant.CEPH_OSD_NOD_NUM.equals(fvo.getNodeType())).collect(Collectors.toList());
        monNodes = nodes.stream().filter(fvo -> ServerCmdConstant.CEPH_MON_NOD_NUM.equals(fvo.getNodeType())).collect(Collectors.toList());
        if (!nodes.isEmpty()){
            monNode = monNodes.get(0);
        }
        cephClusterBpo.setDeleting(clusterId,1);
        return true;
    }

    @Override
    public boolean needCheck() {
        return false;
    }

    @Override
    public void executeFailed(Exception e) {
        cephClusterBpo.setDeleting(clusterId,0);
    }

    @Override
    public void onExecute() throws Exception {
        //删除绑定集群的sc
        ServerUtils.deletePvByCephClusterId(clusterId);
        //先删除osd节点,后删除mon节点
        for (CephNodeInfoVo nvo : osdNodes){
            try {
                ServerUtils.deleteCephNode(nvo,monNode,nodes);
                cephNodeBpo.deleteNode(nvo);
                nodes.remove(nvo);
            } catch (Exception e) {
                throw new Exception("删除节点失败" + e);
            }
        }
        //删除pools
        ServerUtils.deletePools(cephPoolBpo.getAllPools(clusterId));
        //删除mon节点
        for (CephNodeInfoVo nvo : monNodes){
            try {
                ServerUtils.deleteCephNode(nvo,nvo,nodes);
                nodes.remove(nvo);
                cephNodeBpo.deleteNode(nvo);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
    }

    @Override
    public void successfullyExecuted() {
        cephClusterBpo.deleteCephCluster(clusterId);
        //删除集群下所有存储池
        cephPoolBpo.deleteByClusterId(clusterId);
    }
}
