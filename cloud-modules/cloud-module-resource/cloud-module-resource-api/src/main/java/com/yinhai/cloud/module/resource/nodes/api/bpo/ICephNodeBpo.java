package com.yinhai.cloud.module.resource.nodes.api.bpo;


import com.yinhai.cloud.module.resource.nodes.api.vo.CephNodeInfoVo;

import java.util.List;

/**
 * @author liuyi02
 */
public interface ICephNodeBpo {
    /**
     * 查询所有的节点不分页
     */
    List<CephNodeInfoVo> getAllCephNode(Long id);

    /**
     * 检验当前id和当前node名是否被使用
     * @param vo
     * @return
     */
    boolean checkNodeInfoRepeat(CephNodeInfoVo vo);

    /**
     * 根据nodeid查询节点信息
     * @param nodeId
     * @return
     */
    CephNodeInfoVo queryNodeById(Long nodeId);

    /**
     * 保存ceph节点
     * @param vo
     * @return
     */
    CephNodeInfoVo saveCephNodeInfo(CephNodeInfoVo vo);

    /**
     * 得到多有mon节点
     * @return
     */
    List<CephNodeInfoVo> getMonNodeInfo(Long id);

    /**
     * 修改节点信息
     * @param vo
     */
    void update(CephNodeInfoVo vo);

    /**
     * 获取集群所有osd节点数量
     * @param clusterId
     * @return
     */
    int getOsdNodeNum(Long clusterId);

    /**
     * 获取集群下所有mon节点个数
     * @param clusterId
     * @return
     */
    int getMonNodeNum(Long clusterId);

    /**
     * 删除节点
     * @param vo
     */
    void deleteNode(CephNodeInfoVo vo);

    /**
     * 修改操作状态
     * @param nodeId
     */
    void setNodeOperatingState(Long nodeId,Integer operation);

    /**
     * 查询集群下所有mds节点
     * @param id
     * @return
     */
    List<CephNodeInfoVo> getAllMds(Long id);

    /**
     * 查询集群下所有osd节点详细信息
     * @param volumeCloudStorageId
     * @return
     */
    List<CephNodeInfoVo> getOsdNodeInfo(Long volumeCloudStorageId);

    /**
     * 资源发生变化（修改osd可用磁盘大小）
     * @param dick
     * @param volumeCloudStorageId
     */
    void setResourcesChange(double dick, Long volumeCloudStorageId,boolean add);
}
