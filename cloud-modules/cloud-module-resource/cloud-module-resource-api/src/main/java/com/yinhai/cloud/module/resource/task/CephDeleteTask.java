package com.yinhai.cloud.module.resource.task;

import com.yinhai.cloud.module.resource.cluster.api.bpo.ICephClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.bpo.ICephPoolBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.CephPoolVo;
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
public class CephDeleteTask extends AbstractOperateTask {
    private final static Logger logger = LoggerFactory.getLogger(CephDeleteTask.class);
    private ICephNodeBpo cephNodeBpo;//进一步业务操作
    private ICephClusterBpo cephClusterBpo;
    private CephNodeInfoVo vo;//节点信息
    private List<CephNodeInfoVo> monNodes,nodes;//集群列表信息
    protected ICephPoolBpo cephPoolBpo = ServiceLocator.getService(ICephPoolBpo.class);

    public CephDeleteTask(CephNodeInfoVo vo, ICephNodeBpo cephNodeBpo,ICephClusterBpo cephClusterBpo) {
        this.vo = vo;
        this.cephNodeBpo = cephNodeBpo;
        this.cephClusterBpo = cephClusterBpo;
    }

    @Override
    public boolean needCheck(){
        return false;
    }

    @Override
    public boolean beforeExecute() {
        monNodes = cephNodeBpo.getMonNodeInfo(vo.getClusterId()).stream().filter(fvo -> !fvo.getNodeIP().equals(vo.getNodeIP())).collect(Collectors.toList());
        nodes = cephNodeBpo.getAllCephNode(vo.getClusterId());
        cephNodeBpo.setNodeOperatingState(vo.getNodeId(),1);//设置操作状态为被操作
        return true;
    }

    @Override
    public void executeFailed(Exception e) {
        return;
    }

    /**
     * 删除节点
     */
    @Override
    public void successfullyExecuted() {
//        cephNodeBpo.setNodeOperatingState(vo.getNodeId(),NodeState.OPEATE_NONE);//设置操作状态为未操作
        cephNodeBpo.deleteNode(vo);
        ServerUtils.refreshResources(vo.getClusterId());//刷新节点对应集群资源
    }

    @Override
    public void onExecute() throws Exception {
        //将对应节点从集群中去除
        if (nodes.size() == 1){
            //删除pools
            ServerUtils.deletePools(cephPoolBpo.getAllPools(vo.getClusterId()));
            //删除挂载了集群的sc
            ServerUtils.deletePvByCephClusterId(vo.getClusterId());
            //表示最后一个节点，该节点肯定为mon节点
            ServerUtils.deleteCephNode(vo,vo,nodes);
        }else {
            ServerUtils.deleteCephNode(vo,monNodes.get(0),nodes);
        }
    }
}
