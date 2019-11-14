package com.yinhai.cloud.module.resource.nodes.impl.dao.impl;


import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.nodes.api.vo.CephNodeInfoVo;
import com.yinhai.cloud.module.resource.nodes.impl.dao.ICephNodeDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.CephNodeInfoPo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * @author liuyi02
 */
public class CephNodeDaoImpl extends HibernateDAO<CephNodeInfoPo> implements ICephNodeDao {

    @Override
    public List<CephNodeInfoPo> getAllNodes() {
        String hql = "from CephNodeInfoPo order by nodeType";
        return super.find(hql);
    }

    @Override
    public CephNodeInfoPo getNodeById(Long nodeId) {
        return get(nodeId);
    }

    @Override
    public boolean checkNodeNameRepeat(String nodeName) {
        return getCount("from CephNodeInfoPo where nodeName = ?", null, nodeName) >= 1;
    }

    @Override
    public boolean checkNodeIpRepeat(String nodeIP) {
        return getCount("from CephNodeInfoPo where nodeIP = ?", null, nodeIP) >= 1;
    }

    @Override
    public List<CephNodeInfoPo> getAllMonNodes(Long id) {
        String hql = "from CephNodeInfoPo where nodeType = '" + ServerCmdConstant.CEPH_MON_NOD_NUM + "' and clusterId=?";
        return super.find(hql,id);
    }

    @Override
    public List<CephNodeInfoPo> getAllOsdNodes(Long id) {
        String hql = "from CephNodeInfoPo where nodeType = '" + ServerCmdConstant.CEPH_OSD_NOD_NUM + "' and clusterId=?";
        return super.find(hql,id);
    }

    @Override
    public void setResourcesChange(double dick, Long cloudId,boolean add) {
        String hql = "update CephNodeInfoPo set nodeDiskToTalMbIdealState=nodeDiskToTalMbIdealState" + (add?"+":"-") + "? where clusterId=? and nodeType=?";
        super.update(hql,dick,cloudId,ServerCmdConstant.CEPH_OSD_NOD_NUM);
    }

    @Override
    public List<CephNodeInfoPo> getAllNodesByClusterId(Long id) {
        String hql = "from CephNodeInfoPo where clusterId=? order by nodeType";
        return super.find(hql,id);
    }

    @Override
    public CephNodeInfoVo saveNode(CephNodeInfoVo vo) {
        final CephNodeInfoPo po = new CephNodeInfoPo();
        super.save(vo.toPO(po));
        return po.toVo(new CephNodeInfoVo());
    }

    @Override
    public void updateCeph(CephNodeInfoPo toPO) {
        CephNodeInfoPo po = getNodeById(toPO.getNodeId());
        if (!ValidateUtil.isEmpty(toPO.getNodeIP())){
            po.setNodeIP(toPO.getNodeIP());
        }
        if (!ValidateUtil.isEmpty(toPO.getNodeCreateUser())){
            po.setNodeCreateUser(toPO.getNodeCreateUser());
        }
        if (!ValidateUtil.isEmpty(toPO.getNodeRunningState())){
            po.setNodeRunningState(toPO.getNodeRunningState());
        }
        if (!ValidateUtil.isEmpty(toPO.getNodeType())){
            po.setNodeType(toPO.getNodeType());
        }
        if (!ValidateUtil.isEmpty(toPO.getNodeDiskToTalMb())){
            po.setNodeDiskToTalMb(toPO.getNodeDiskToTalMb());
        }
        if (!ValidateUtil.isEmpty(toPO.getNodeDiskToTalMbIdealState())){
            po.setNodeDiskToTalMbIdealState(toPO.getNodeDiskToTalMbIdealState());
        }
        if (!ValidateUtil.isEmpty(toPO.getNodeCName())){
            po.setNodeCName(toPO.getNodeCName());
        }
        if (!ValidateUtil.isEmpty(toPO.getCephOperation())){
            po.setCephOperation(toPO.getCephOperation());
        }
        if (!ValidateUtil.isEmpty(toPO.getClusterId())){
            po.setClusterId(toPO.getClusterId());
        }
        if (!ValidateUtil.isEmpty(toPO.getNodeName())){
            po.setNodeName(toPO.getNodeName());
        }
        if (!ValidateUtil.isEmpty(toPO.getNodePassword())){
            po.setNodePassword(toPO.getNodePassword());
        }
        if (!ValidateUtil.isEmpty(toPO.getNodePath())){
            po.setNodePath(toPO.getNodePath());
        }
        if (!ValidateUtil.isEmpty(toPO.getNodeSSHPort())){
            po.setNodeSSHPort(toPO.getNodeSSHPort());
        }
        if (!ValidateUtil.isEmpty(toPO.getIsMds())){
            po.setIsMds(toPO.getIsMds());
        }
        super.update(po);
    }

    @Override
    public int getOsdNodeNum(Long clusterId) {
        String hql = "select count(*) from CephNodeInfoPo where clusterId=? and nodeType=?";
        return getCount(hql, null, clusterId, ServerCmdConstant.CEPH_OSD_NOD_NUM).intValue();
    }

    @Override
    public int getMonNodeNum(Long clusterId) {
        String hql = "select count(*) from CephNodeInfoPo where clusterId=? and nodeType=?";
        return getCount(hql, null, clusterId, ServerCmdConstant.CEPH_MON_NOD_NUM).intValue();
    }

    @Override
    public void deleteNode(CephNodeInfoPo o) {
        super.delete(o);
    }

    @Override
    public List<CephNodeInfoPo> getAllMdsNodes(Long id) {
        String hql = "from CephNodeInfoPo where clusterId=? and isMds=? order by nodeType";
        return super.find(hql,id,1);
    }
}
