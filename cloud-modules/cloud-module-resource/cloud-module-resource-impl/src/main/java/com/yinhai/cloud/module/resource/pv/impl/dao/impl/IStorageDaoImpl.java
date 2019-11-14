package com.yinhai.cloud.module.resource.pv.impl.dao.impl;

import com.yinhai.cloud.module.resource.pv.impl.dao.IStorageDao;
import com.yinhai.cloud.module.resource.pv.impl.po.StoragePo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pengwei on 2018/6/22.
 */
public class IStorageDaoImpl extends HibernateDAO<StoragePo> implements IStorageDao {

    @Override
    public Long saveStorage(StoragePo po) {
        Serializable storageId = super.save(po);

        return (Long) storageId;
    }

    @Override
    public List<StoragePo> queryAllStorages() {
        final String hql = "from StoragePo storagePo";
        final List<StoragePo> storageList = super.find(hql);

        return storageList;
    }

    @Override
    public StoragePo queryStorageById(Long storageId) {
        String hql = "from StoragePo storagePo where storageId = ?";
        StoragePo storagePo = super.get(hql, storageId);
        return storagePo;
    }

    @Override
    public StoragePo checkStorageIsExist(String hostip, String rootPath) {
        String hql = "from StoragePo storagePo where hostIp = ? and hostRootPath = ?";
        StoragePo storagePo = super.get(hql, hostip, rootPath);
        return storagePo;
    }

    @Override
    public List<StoragePo> checkInstalledStorage(String hostip) {
        String hql = "from StoragePo storagePo where hostIp = ? ";
        List<StoragePo> storagePos = super.find(hql, hostip);
        return storagePos;
    }

    @Override
    public void deleteStorage(Long storageId) {
        String hql = "delete StoragePo  where storageId = ? ";
        super.delete(hql,storageId);
    }
}
