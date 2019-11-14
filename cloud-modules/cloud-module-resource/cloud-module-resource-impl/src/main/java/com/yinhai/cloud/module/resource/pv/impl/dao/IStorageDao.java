package com.yinhai.cloud.module.resource.pv.impl.dao;

import com.yinhai.cloud.module.resource.pv.impl.po.StoragePo;

import java.util.List;

/**
 * Storage Dao
 *
 * @author pengwei
 */
public interface IStorageDao {

    Long saveStorage(StoragePo po);

    List<StoragePo> queryAllStorages();

    StoragePo queryStorageById(Long storageId);

    /**
     * 检查NFS挂载目录是否已存在
     *
     * @param hostip
     * @param rootPath
     * @return
     */
    StoragePo checkStorageIsExist(String hostip, String rootPath);

    /**
     * 检查注册的主机是否已安装NFS
     *
     * @param hostip
     * @return
     */
    List<StoragePo> checkInstalledStorage(String hostip);

    void deleteStorage(Long storageId);
}
