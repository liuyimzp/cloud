package com.yinhai.cloud.module.resource.nodes.impl.dao;

import com.yinhai.cloud.module.resource.nodes.api.vo.CephNodeInfoVo;
import com.yinhai.cloud.module.resource.nodes.impl.po.CephNodeInfoPo;

import java.util.List;

/**
 * @author liuyi02
 */
public interface ICephNodeDao {


    /**
     * 查询所有节点
     * @return
     */
    List<CephNodeInfoPo> getAllNodes();

    /**
     * 根据nodeid获得节点信息
     * @param nodeId
     * @return
     */
    CephNodeInfoPo getNodeById(Long nodeId);

    /**
     * 检查节点name是否存在
     * @param nodeName
     * @return
     */
    boolean checkNodeNameRepeat(String nodeName);

    /**
     * 检查节点ip是否存在
     * @param nodeIP
     * @return
     */
    boolean checkNodeIpRepeat(String nodeIP);

    /**
     * 获得所有mon节点
     * @return
     */
    List<CephNodeInfoPo> getAllMonNodes(Long id);

    /**
     * 通过集群id查询集群下所有节点
     * @param id
     * @return
     */
    List<CephNodeInfoPo> getAllNodesByClusterId(Long id);

    /**
     * 保存ceph节点
     * @param vo
     * @return
     */
    CephNodeInfoVo saveNode(CephNodeInfoVo vo);

    /**
     * 修改数据库节点信息
     * @param toPO
     */
    void updateCeph(CephNodeInfoPo toPO);

    /**
     * 统计某集群下所有的osd节点数
     * @param clusterId
     * @return
     */
    int getOsdNodeNum(Long clusterId);

    /**
     * 统计某集群下所有mon节点个数
     * @param clusterId
     * @return
     */
    int getMonNodeNum(Long clusterId);

    /**
     * 删除节点数据
     * @param toPO
     */
    void deleteNode(CephNodeInfoPo toPO);

    /**
     * 查询数据库中集群下所有的mds节点
     * @param id
     */
    List<CephNodeInfoPo> getAllMdsNodes(Long id);

    /**
     * 查询所有osd节点
     * @param id
     * @return
     */
    List<CephNodeInfoPo> getAllOsdNodes(Long id);

    /**
     * 将数据库中集群下所有osd节点可用资源减少
     * @param dick
     * @param cloudId
     */
    void setResourcesChange(double dick, Long cloudId,boolean add);
}
