package com.yinhai.cloud.module.resource.cluster.api.bpo;

import com.yinhai.cloud.module.resource.cluster.api.vo.ClusterVo;
import com.yinhai.cloud.module.resource.nodes.api.vo.OperateRunningServer;

import java.util.List;

/**
 * 集群管理接口
 *
 * @author zhaokai
 */
public interface IClusterBpo {


    /**
     * 查询指定ID的集群信息
     *
     * @param id 集群ID
     * @return ClusterVo
     */
    ClusterVo getClusterById(Long id);


    /**
     * 删除指定Id的集群
     *
     * @param id 集群ID
     */
    void deleteClusterById(Long id);

    /**
     * 更新集群信息
     *
     * @param clusterVo 集群信息，必须包含ID
     * @return false：集群标识重复，true：更新成功
     */
    Boolean updateClusterById(ClusterVo clusterVo);

    /**
     * 节点停用/启用 更新集群的内存和cpu
     * @param id
     * @param mem
     * @param cpu
     * @param isAdd
     */
    void updateClusterMemCpu(Long id, Double mem, Double cpu, Boolean isAdd);

    /**
     * 添加集群信息
     *
     * @param clusterVo
     * @return
     */
    ClusterVo addNewCluster(ClusterVo clusterVo);

    /**
     * 检查集群标识是否重复
     *
     * @param addIdentify 需要检查的集群标识
     * @return true 集群标识重复 false集群标识未重复
     */
    Boolean checkIdentifyRepeat(String addIdentify);


    /**
     * 集群节点数量加一
     *
     * @param clusterId
     */
    void plusOneClusterNodeAmountById(Long clusterId);

    /**
     * 节点数量减一
     *
     * @param clusterId
     */
    void minusOneClusterNodeAmountById(Long clusterId);

    /**
     * 查询所有集群基础信息，不包含CPU、磁盘、内存信息
     *@param userid
     * @return
     */
    List<ClusterVo> queryAllClusterBasicInfo(Long userid);


    /**
     * 非正在删除的集群
     *
     * @return
     */
    List<ClusterVo> queryAllNotBeenDeletingCluster();

    /**
     * 更新集群状态为正在删除中
     *
     * @param clusterId
     * @param operateRunningIp
     */
    void updateClusterAsDeleting(Long clusterId, OperateRunningServer operateRunningIp);

    /**
     * 获取集群资源信息
     *
     * @param clusterId
     * @return
     */
    ClusterVo queryClusterStatistics(Long clusterId);

    /**
     * 获取集群列表，没有资源统计信息
     *
     * @return
     */
    List<ClusterVo> queryAllClusterWithoutStatistic();

    void refreshResource(Long clusterId);

    boolean checkHAIp(String ip);
}
