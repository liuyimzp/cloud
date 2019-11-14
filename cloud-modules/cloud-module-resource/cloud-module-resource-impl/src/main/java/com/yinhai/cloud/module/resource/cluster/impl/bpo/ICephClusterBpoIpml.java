package com.yinhai.cloud.module.resource.cluster.impl.bpo;

import com.yinhai.cloud.module.resource.cluster.api.bpo.ICephClusterBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.CephClusterVo;
import com.yinhai.cloud.module.resource.cluster.impl.dao.ICephClusterDao;
import com.yinhai.cloud.module.resource.cluster.impl.po.CephClusterPo;
import com.yinhai.cloud.module.resource.constants.ClusterState;
import com.yinhai.cloud.module.resource.constants.NodeState;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.nodes.impl.dao.ICephNodeDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.CephNodeInfoPo;
import com.yinhai.cloud.module.user.api.bpo.IUserAuthorityBpo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuyi02
 */
public class ICephClusterBpoIpml extends TaBaseBpo implements ICephClusterBpo {

    @Resource
    private ICephClusterDao cephClusterDao;

    @Resource
    private IUserAuthorityBpo userAuthorityBpo;

    @Resource
    private ICephNodeDao cephNodeDao;

    @Override
    public CephClusterVo getClusterById(Long id) {
        CephClusterPo po = cephClusterDao.getClusterById(id);
        return po.toVo(new CephClusterVo());
    }

    @Override
    public List<CephClusterVo> queryAllClusterBasicInfo() {
        List<CephClusterPo> cephClusterPos;
        cephClusterPos = cephClusterDao.getClusterList();
        if (cephClusterPos == null){
            cephClusterPos = new ArrayList<>();
        }
        return parsingList(cephClusterPos);
    }

    @Override
    public CephClusterVo saveCeph(CephClusterVo vo) {
        CephClusterPo po = cephClusterDao.saveCeph(vo.toPO(new CephClusterPo()));
        return po.toVo(new CephClusterVo());
    }

    @Override
    public boolean checkCluster(String identify) {
        return cephClusterDao.getCountTag(identify);
    }

    @Override
    public void updateCluster(CephClusterVo vo) {
        cephClusterDao.updateClusterByPo(vo.toPO(new CephClusterPo()));
    }

    @Override
    public void deleteCephCluster(Long id) {
        cephClusterDao.deleteCluster(id);
    }

    @Override
    public void setDeleting(Long clusterId, int i) {
        CephClusterVo vo = getClusterById(clusterId);
        vo.setDeleting(i);
        updateCluster(vo);
    }

    @Override
    public List<CephClusterVo> queryAllClusterByClusterId(Long clusterId) {
        return parsingList(cephClusterDao.getClusterByK8sClusterId(clusterId));
    }

    /**
     * 将cephClusterPo集合解析成CephClusterVo集合
     * @param cephClusterPos
     * @return
     */
    private List<CephClusterVo> parsingList(List<CephClusterPo> cephClusterPos){
        return cephClusterPos.stream().map(po -> {
            CephClusterVo vo = po.toVo(new CephClusterVo());
            List<CephNodeInfoPo> cPos = cephNodeDao.getAllNodesByClusterId(vo.getId());
            int runningState = ClusterState.CLUSTER_RUNNING_SUCCESSFULLY;
            String runningStateDesc = "运行正常";
            int nodeNum = cPos.size();
            Double memoryTotal = 0.0;
            Double memoryAvaiLabel = 0.0;
            //判断集群状态
            if (cPos.isEmpty()){
                runningState = ClusterState.CLUSTER_NOT_RUNNING;
                runningStateDesc = "未运行";
                vo.setMemoryTotal(0.0);
                vo.setMemoryAvaiLabel(0.0);
            }
            else {
                memoryTotal = cPos.stream().filter(cpo -> ServerCmdConstant.CEPH_OSD_NOD_NUM.equals(cpo.getNodeType()) && cpo != null && cpo.getNodeRunningState() == NodeState.RUNNING_SUCCESS).mapToDouble(CephNodeInfoPo::getNodeDiskToTalMb).sum();
                memoryAvaiLabel = cPos.stream().filter(cpo -> ServerCmdConstant.CEPH_OSD_NOD_NUM.equals(cpo.getNodeType()) && cpo != null  && cpo.getNodeRunningState() == NodeState.RUNNING_SUCCESS).mapToDouble(CephNodeInfoPo::getNodeDiskToTalMbIdealState).sum();
                for (CephNodeInfoPo cPo : cPos){
                    int runState = cPo.getNodeRunningState();
                    if (runState != 1 && runState != 0 && runState != 2 && runState != 3){
                        runningState = ClusterState.CLUSTER_RUNNING_FAILED;
                        runningStateDesc = "运行异常";
                    }
                }
                //计算集群资源
                vo.setMemoryTotal(memoryTotal/po.getPoolSize());
                vo.setMemoryAvaiLabel(memoryAvaiLabel/po.getPoolSize());
            }
            vo.setRunningState(runningState);
            vo.setRunningStateDesc(runningStateDesc);
            vo.setNumOfNodes(nodeNum);
            boolean memoryTotalb = vo.getMemoryTotal() != po.getMemoryTotal();
            boolean memoryA = vo.getMemoryAvaiLabel() != po.getMemoryAvaiLabel();
            boolean running = vo.getRunningState() != po.getRunningState();
            boolean nodeChenge = vo.getNumOfNodes() != po.getNumOfNodes();
            //如果数据与最新计算的数据不同则同步数据到数据库
            if(memoryTotalb || memoryA || running || nodeChenge){
                po.setMemoryAvaiLabel(vo.getMemoryAvaiLabel());
                po.setMemoryTotal(vo.getMemoryTotal());
                po.setRunningState(vo.getRunningState());
                po.setNumOfNodes(nodeNum);
                cephClusterDao.updateClusterByPo(po);
            }
            DecimalFormat df = new DecimalFormat("######0.00");
            vo.setMemoryTotal(Double.valueOf(df.format(vo.getMemoryTotal())));
            vo.setMemoryAvaiLabel(Double.valueOf(df.format(vo.getMemoryAvaiLabel())));
            return vo;
        }).collect(Collectors.toList());
    }
}
