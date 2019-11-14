package com.yinhai.cloud.module.resource.nodes.impl.bpo;

import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.module.resource.nodes.api.bpo.ICephNodeBpo;
import com.yinhai.cloud.module.resource.nodes.api.vo.CephNodeInfoVo;
import com.yinhai.cloud.module.resource.nodes.impl.dao.ICephNodeDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.CephNodeInfoPo;
import com.yinhai.cloud.module.resource.util.ServerUtils;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;
import com.yinhai.modules.org.ta3.domain.dao.IUserDao;
import com.yinhai.modules.org.ta3.domain.po.impl.UserPo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuyi02
 */
public class CephNodeBpoImpl extends TaBaseBpo implements ICephNodeBpo {
    @Resource
    private ICephNodeDao cephNodeDao;

    @Autowired
    private IUserDao userDao;

    private static final String CODE_KEY_NODE_RUNNING_STATE_DESC = "NODE_RUN_STATE_DESC";
    @Override
    public List<CephNodeInfoVo> getAllCephNode(Long id) {
        List<CephNodeInfoPo> cpos = cephNodeDao.getAllNodesByClusterId(id);
        return parsingNodes(cpos);
    }

    @Override
    public boolean checkNodeInfoRepeat(CephNodeInfoVo vo) {
        if (vo.getNodeId() != null){
            //表示编辑
            CephNodeInfoVo newVo = queryNodeById(vo.getNodeId());
            boolean ipSameOld = newVo.getNodeIP().equals(vo.getNodeIP());
            boolean nameSameOld = newVo.getNodeName().equals(vo.getNodeName());
            return (!nameSameOld && cephNodeDao.checkNodeNameRepeat(vo.getNodeName())) || (!ipSameOld && cephNodeDao.checkNodeIpRepeat(vo.getNodeIP()));
        }else{
            return cephNodeDao.checkNodeNameRepeat(vo.getNodeName()) || cephNodeDao.checkNodeIpRepeat(vo.getNodeIP());
        }
    }

    @Override
    public CephNodeInfoVo queryNodeById(Long nodeId) {
        CephNodeInfoPo cpo = cephNodeDao.getNodeById(nodeId);
        return initCeph(cpo.toVo(new CephNodeInfoVo()));
    }

    @Override
    public CephNodeInfoVo saveCephNodeInfo(CephNodeInfoVo vo) {
        return cephNodeDao.saveNode(vo);
    }

    @Override
    public List<CephNodeInfoVo> getMonNodeInfo(Long id) {
        List<CephNodeInfoPo> cpos = cephNodeDao.getAllMonNodes(id);
        return parsingNodes(cpos);
    }

    @Override
    public List<CephNodeInfoVo> getOsdNodeInfo(Long id) {
        List<CephNodeInfoPo> cpos = cephNodeDao.getAllOsdNodes(id);
        return parsingNodes(cpos);
    }

    @Override
    public void setResourcesChange(double dick, Long cloudId,boolean add) {
        cephNodeDao.setResourcesChange(dick,cloudId,add);
    }

    @Override
    public void update(CephNodeInfoVo vo) {
        cephNodeDao.updateCeph(vo.toPO(new CephNodeInfoPo()));
    }

    @Override
    public int getOsdNodeNum(Long clusterId) {
        return cephNodeDao.getOsdNodeNum(clusterId);
    }

    @Override
    public int getMonNodeNum(Long clusterId) {
        return cephNodeDao.getMonNodeNum(clusterId);
    }

    @Override
    public void deleteNode(CephNodeInfoVo vo) {
        cephNodeDao.deleteNode(vo.toPO(new CephNodeInfoPo()));
        //删除之后需要将分配空间分配到其他节点
        ServerUtils.setNodeResource(vo);
    }

    @Override
    public void setNodeOperatingState(Long nodeId,Integer operation) {
        CephNodeInfoVo vo = queryNodeById(nodeId);
        vo.setCephOperation(operation);
        update(vo);
    }

    @Override
    public List<CephNodeInfoVo> getAllMds(Long id) {
        return cephNodeDao.getAllMdsNodes(id).stream().map(nvo->{
            CephNodeInfoVo vo = nvo.toVo(new CephNodeInfoVo());
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 将ceph节点数据与界面数据相符
     * @param vo
     * @return
     */
    private CephNodeInfoVo initCeph(CephNodeInfoVo vo){
        String runningDesc = CodeTableUtil.getDesc(CODE_KEY_NODE_RUNNING_STATE_DESC, String.valueOf(vo.getNodeRunningState()));
        vo.setNodeRunningText(runningDesc);
        switch (vo.getNodeRunningState()) {
            case 1:
                vo.setNodeRunningStely(ResourceConstant.DISPLAY_STATE_STYLE_SUCCESS);
                vo.setNodeRunningText("正在运行");
                break;
            case 0:
                vo.setNodeRunningStely(ResourceConstant.DISPLAY_STATE_STYLE_LOADING);
                vo.setNodeRunningText("初始化");
                break;
            case 2:
                vo.setNodeRunningStely(ResourceConstant.DISPLAY_STATE_STYLE_LOADING);
                vo.setNodeRunningText("删除中");
                break;
            case 3:
                vo.setNodeRunningStely(ResourceConstant.DISPLAY_STATE_STYLE_DISABLED);
                vo.setNodeRunningText("已停止");
                break;
            case 4:
                vo.setNodeRunningStely(ResourceConstant.DISPLAY_STATE_STYLE_ERROR);
                vo.setNodeRunningText("运行失败");
                break;
            default:
                vo.setNodeRunningStely(ResourceConstant.DISPLAY_STATE_STYLE_LOADING);
                vo.setNodeRunningText("初始化");
                break;
        }
        return vo;
    }

    private List<CephNodeInfoVo> parsingNodes(List<CephNodeInfoPo> cpos){
        return cpos.stream().map(it ->{
            CephNodeInfoVo cephNodeInfoVo = it.toVo(new CephNodeInfoVo());
            final UserPo userPo = userDao.getUserByUserId(it.getNodeCreateUser());
            cephNodeInfoVo.setCreatUser(userPo.getName());
            initCeph(cephNodeInfoVo);
            return cephNodeInfoVo;
        }).collect(Collectors.toList());
    }
}
