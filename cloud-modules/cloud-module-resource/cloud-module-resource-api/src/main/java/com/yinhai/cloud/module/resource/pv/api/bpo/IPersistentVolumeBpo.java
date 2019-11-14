package com.yinhai.cloud.module.resource.pv.api.bpo;

import com.yinhai.cloud.module.resource.pv.api.vo.DiskVo;
import com.yinhai.cloud.module.resource.pv.api.vo.PvVo;
import com.yinhai.cloud.module.resource.pv.api.vo.StorageVo;

import java.util.List;
import java.util.Map;

/**
 * 持久化存储卷 接口
 *
 * @author jianglw
 */
public interface IPersistentVolumeBpo {

    String SERVICEKEY = "pvBpo";
    /**
     * 获取节点下所有PV
     *
     * @return
     */
    List<PvVo> queryAll(String clusterId);

    /**
     * 根据id获取一个vo
     *
     * @param pvId
     * @return
     */
    PvVo queryByPvId(Long pvId);
    PvVo queryByStorageId(Long storageId);

    /**
     * 新增一个PV
     *
     * @param vo
     */
    void addPv(PvVo vo) throws Exception;

    /**
     * 删除一个PV
     *
     * @param vo
     */
    void deletePv(PvVo vo) throws Exception;


    /**
     * 更新一个PV
     *
     * @param vo
     */
    void updatePv(PvVo vo) throws Exception;

    /**
     * 获取磁盘目录挂载列表
     *
     * @param vo
     * @throws Exception
     */
    List<DiskVo> getDiskMountList(StorageVo vo) throws Exception;

    /**
     * 添加网盘存储
     *
     * @param vo
     * @return 网盘编号
     * @throws Exception
     */
    Long addStorage(StorageVo vo) throws Exception;

    /**
     * 获取所有网盘信息
     *
     * @return
     */
    List<StorageVo> queryAllStorages() throws Exception;

    /**
     * 检查目录是否可用 以及空间大小
     * @return
     */
    List<DiskVo> checkStorages(StorageVo vo) throws Exception ;

    /**
     * 删除云目录
     * @param vo
     * @throws Exception
     */
    void deleteStorage(StorageVo vo) throws Exception ;

    /**
     * 强制删除云目录，用于无法连接云存储时删除本地数据库数据
     * @param vo
     */
    void deleteStorageForce(StorageVo vo);

    /**
     * 检验pool是否被pv挂载
     * @param poolId
     * @return
     */
    boolean queryByPoolId(Long poolId);

    /**
     * 停止节点nfs服务
     * @param vo
     */
    void stopNfs(StorageVo vo) throws Exception;

    /**
     * 启动节点nfs服务
     * @param vo
     */
    void startNfs(StorageVo vo) throws Exception;

    /**
     * 得到某应用挂载的云目录
     * @param id
     * @return
     */
    StorageVo getStorageById(Long id);

    /**
     * 得到应用挂载的pv
     * @param id
     * @return
     */
    List<PvVo> getPvByAppId(Long id);
}
