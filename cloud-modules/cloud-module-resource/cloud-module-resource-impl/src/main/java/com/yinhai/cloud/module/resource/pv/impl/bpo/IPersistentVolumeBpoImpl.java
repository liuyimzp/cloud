package com.yinhai.cloud.module.resource.pv.impl.bpo;

import com.yinhai.cloud.core.api.entity.MsgVO;
import com.yinhai.cloud.core.api.util.DataEncrypt;
import com.yinhai.cloud.module.application.api.bpo.IAppPVBpo;
import com.yinhai.cloud.module.resource.cluster.api.vo.CephPoolVo;
import com.yinhai.cloud.module.resource.cluster.impl.dao.ICephPoolDao;
import com.yinhai.cloud.module.resource.cluster.impl.dao.IClusterDao;
import com.yinhai.cloud.module.resource.cluster.impl.po.CephPoolPo;
import com.yinhai.cloud.module.resource.constants.ServerCmdConstant;
import com.yinhai.cloud.module.resource.nodes.api.vo.NodeBasicInfoVo;
import com.yinhai.cloud.module.resource.nodes.impl.dao.INodeDao;
import com.yinhai.cloud.module.resource.nodes.impl.po.NodeBasicInfoPo;
import com.yinhai.cloud.module.resource.pv.api.bpo.IPersistentVolumeBpo;
import com.yinhai.cloud.module.resource.pv.api.constants.PvConstants;
import com.yinhai.cloud.module.resource.pv.api.vo.DiskVo;
import com.yinhai.cloud.module.resource.pv.api.vo.PvVo;
import com.yinhai.cloud.module.resource.pv.api.vo.StorageVo;
import com.yinhai.cloud.module.resource.pv.impl.dao.IPersistentVolumeDao;
import com.yinhai.cloud.module.resource.pv.impl.dao.IStorageDao;
import com.yinhai.cloud.module.resource.pv.impl.po.PvPo;
import com.yinhai.cloud.module.resource.pv.impl.po.StoragePo;
import com.yinhai.cloud.module.resource.util.DiskUtil;
import com.yinhai.cloud.module.resource.util.NfsClientProvisionerYaml;
import com.yinhai.cloud.module.resource.util.NfsInstall;
import com.yinhai.cloud.module.resource.util.ServerUtils;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import com.yinhai.modules.codetable.api.util.CodeTableUtil;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jianglw
 */
public class IPersistentVolumeBpoImpl extends TaBaseBpo implements IPersistentVolumeBpo {

    @Autowired
    private IPersistentVolumeDao pvDao;

    @Autowired
    private IStorageDao storageDao;

    @Autowired
    private IClusterDao clusterDao;

    @Autowired
    private INodeDao nodeDao;

    @Autowired
    private IAppPVBpo appPVBpo;

    @Autowired
    private ICephPoolDao cephPoolDao;

    @Resource
    private SessionFactory sessionFactory;

    @Override
    public List<PvVo> queryAll(String clusterId) {
        return pvDao.queryAll().stream().filter(it -> it.getClusterId().equals(clusterId)).map(it -> {
            final PvVo pvVo = it.toVo(new PvVo());
            pvVo.setVolumeTypeDesc(CodeTableUtil.getDesc(PvConstants.PV_TYPE_CODE, pvVo.getVolumeType()));
            final String clusterName = clusterDao.getClusterById(Long.valueOf(pvVo.getClusterId())).getName();
            pvVo.setClusterName(clusterName);
            final String stateDesc = CodeTableUtil.getDesc(PvConstants.PV_STATE_CODE, pvVo.getVolumeEnableState());
            pvVo.setVolumeEnableStateDesc(stateDesc);
            if (pvVo.getVolumeType().equals(PvConstants.PV_TYPE_REMOTE)) {
//                final String rootPath = storageDao.queryStorageById(pvVo.getVolumeCloudStorageId()).getHostRootPath();;
            }else if (PvConstants.PV_TYPE_CEPH.equals(pvVo.getVolumeType())){
                pvVo.setVolumeStoragePath("/");
            }
            else {
                pvVo.setVolumeStoragePath(pvVo.getVolumeStoragePath().replaceFirst("/", ""));
            }
            return pvVo;
        }).sorted(Comparator.comparing(PvVo::getVolumeId)).collect(Collectors.toList());
    }

    @Override
    public PvVo queryByPvId(final Long pvId) {
        final PvVo pvVo = pvDao.queryByPoId(pvId);
        pvVo.setVolumeTypeDesc(CodeTableUtil.getDesc(PvConstants.PV_TYPE_CODE, pvVo.getVolumeType()));
        final String clusterName = clusterDao.getClusterById(Long.valueOf(pvVo.getClusterId())).getName();
        pvVo.setClusterName(clusterName);
        final String stateDesc = CodeTableUtil.getDesc(PvConstants.PV_STATE_CODE, pvVo.getVolumeEnableState());
        pvVo.setVolumeEnableStateDesc(stateDesc);
        if (pvVo.getVolumeType().equals(PvConstants.PV_TYPE_REMOTE)) {
//            final String rootPath = storageDao.queryStorageById(pvVo.getVolumeCloudStorageId()).getHostRootPath();
//            pvVo.setVolumeStoragePath(pvVo.getVolumeStoragePath().replaceFirst(rootPath, ""));
        }else if (PvConstants.PV_TYPE_CEPH.equals(pvVo.getVolumeType())){
            pvVo.setVolumeStoragePath("/");
        } else {
            pvVo.setVolumeStoragePath(pvVo.getVolumeStoragePath().replaceFirst("/", ""));
        }
        return pvVo;
    }

    @Override
    public PvVo queryByStorageId(Long storageId) {
        PvPo po = pvDao.queryByStorageId(storageId);
        if (po == null) {
            return null;
        }
        return po.toVo(new PvVo());
    }

    @Override
    public void addPv(final PvVo pv) throws Exception {
        generateFullPath(pv);
        //获取集群master节点信息
        List<NodeBasicInfoPo> masters = nodeDao.queryMasterNodesByClusterId(Long.valueOf(pv.getClusterId()));
        if (masters.isEmpty()) {
            throw new Exception("集群未设置主节点，无法创建存储卷");
        }

        if (checkExist(pv)) {
            throw new Exception("在集群'" + clusterDao.getClusterById(Long.valueOf(pv.getClusterId())).getName() + "'中已存在使用'" + pv.getVolumeUuid() + "'标识或" + pv.getVolumeStoragePath() + "路径的PV");
        }

        //新增时查询PV名称是否重复
        List<PvPo> pvPoList = pvDao.queryPvByNameAndId(pv.getVolumeDisplayName(), pv.getClusterId());
        if (!pvPoList.isEmpty()) {
            throw new Exception("集群下存在重复的存储名称");
        }
        NodeBasicInfoVo masterNode = masters.get(0).toVo(new NodeBasicInfoVo());
        if (ServerCmdConstant.PV_TYPE_NFS.equals(pv.getVolumeType())){
            StoragePo storagePo = storageDao.queryStorageById(pv.getVolumeCloudStorageId());
            if (storagePo == null) {
                throw new Exception("未找到所选存储节点");
            }
            //liuyi02 2018/3/1修改
            StorageVo storage = storagePo.toVo(new StorageVo());
            NfsClientProvisionerYaml.createNfsClientProvisioner(masterNode, pv, storage);
        }
        if (ServerCmdConstant.PV_TYPE_CEPH.equals(pv.getVolumeType())){
            CephPoolVo pvo = ServerUtils.createCephPv(masterNode,pv);
            pv.setCephPoolId(pvo.getPoolId());
            pvo.setPoolIsUse(2);
            cephPoolDao.updatePool(pvo.toPO(new CephPoolPo()));
        }

        //默认情况下剩余空间等于最大存储空间
        pv.setVolumeAvailableSpace(pv.getVolumeMaxSpace());

        pvDao.savePo(pv.toPO(new PvPo()));

    }

    /**
     * 处理前台输入的相对路径为绝对路径
     *
     * @param vo
     */
    private void generateFullPath(final PvVo vo) {
        if (vo.getVolumeType().equals(PvConstants.PV_TYPE_REMOTE)) {
            final String rootPath = storageDao.queryStorageById(vo.getVolumeCloudStorageId()).getHostRootPath();
            String fullPath = rootPath + ServerCmdConstant.FILE_SEPARATOR + vo.getVolumeStoragePath();
            fullPath = fullPath.replaceAll("/+", "/");
            vo.setVolumeStoragePath(fullPath);
        } else {
            String fullPath = ServerCmdConstant.FILE_SEPARATOR + vo.getVolumeStoragePath();
            fullPath = fullPath.replaceAll("/+", "/");
            vo.setVolumeStoragePath(fullPath);
        }
    }

    @Override
    public void deletePv(final PvVo pv) throws Exception {
        if (appPVBpo.checkPVUsed(pv.getVolumeId())) {
            throw new Exception("存在使用该pv的应用，不能删除!");
        }
        generateFullPath(pv);
        List<NodeBasicInfoPo> masters = nodeDao.queryMasterNodesByClusterId(Long.valueOf(pv.getClusterId()));

        NodeBasicInfoVo masterNode = masters.get(0).toVo(new NodeBasicInfoVo());

        if (PvConstants.PV_TYPE_REMOTE.equals(pv.getVolumeType())){
            StoragePo storagePo = storageDao.queryStorageById(pv.getVolumeCloudStorageId());
            StorageVo storage = storagePo == null ? null : storagePo.toVo(new StorageVo());
            NfsClientProvisionerYaml.deleteNfsClientProvisioner(masterNode, pv, storage);
        }
        if (PvConstants.PV_TYPE_CEPH.equals(pv.getVolumeType())){
            ServerUtils.deleteCephPv(masterNode,pv);
        }
        pvDao.deletePo(pv.toPO(new PvPo()));

    }


    @Override
    public void updatePv(final PvVo vo) throws Exception {
        if (checkExist(vo)) {
            throw new Exception("在集群'" + clusterDao.getClusterById(Long.valueOf(vo.getClusterId())).getName() + "'中已存在使用'" + vo.getVolumeUuid() + "'标识或" + vo.getVolumeStoragePath() + "路径的PV");
        }
        //新增时查询PV名称是否重复,由于是更新，要排除本身
//        List<PvPo> pvPoList = pvDao.queryPvByNameAndId(vo.getVolumeDisplayName(), vo.getClusterId()).stream().filter(po -> po.getVolumeId().equals(vo.getVolumeId())).collect(Collectors.toList());
//        if (pvPoList.size() > 0) {
//            throw new Exception("集群下存在重复的存储名称");
//        }
        PvVo pvVo = pvDao.queryByPoId(vo.getVolumeId());
        if (!ValidateUtil.areEqual(pvVo.getVolumeMaxSpace(), vo.getVolumeMaxSpace())) {
            if (vo.getVolumeMaxSpace() < pvVo.getVolumeMaxSpace() - pvVo.getVolumeAvailableSpace()) {
                throw new Exception("该pv已使用存储空间超过修改后的最大存储空间!");
            }
            vo.setVolumeAvailableSpace(vo.getVolumeMaxSpace() - pvVo.getVolumeMaxSpace() + pvVo.getVolumeAvailableSpace());
        }
        pvDao.updatePo(vo.toPO(new PvPo()));
    }

    @Override
    public List<DiskVo> getDiskMountList(StorageVo vo) throws Exception {
        return DiskUtil.getServerDiskMountList(vo);
    }

    @Override
    public Long addStorage(StorageVo vo) throws Exception {
        //判重：相同srever，相同挂载目录
        StoragePo existStoragePo = storageDao.checkStorageIsExist(vo.getHostIp(), vo.getHostRootPath());
        if (!ValidateUtil.isEmpty(existStoragePo)) {
            throw new Exception("已存在相同的云盘挂载根目录，请重新输入挂载根目录");
        }

        //判断是否已安装NFS
//        List<StoragePo> existNfs = storageDao.checkInstalledStorage(vo.getHostIp());
        boolean existNfs = NfsInstall.existNfs(vo);
        MsgVO msgVO = null;
        if (existNfs) {
            msgVO = NfsInstall.addMountPath(vo);
            //判断nfs是否已经启动,未启动就重启
            if (!NfsInstall.activeNfs(vo)){
                NfsInstall.restartNfs(vo);
            }
        } else {
            msgVO = NfsInstall.installNfsServer(vo);
        }

        if (msgVO.isSuccess()) {
            StoragePo po = new StoragePo();
            //密码加密存储
            vo.setHostPassword(DataEncrypt.encrypt(vo.getHostPassword()));
            Long storageId = storageDao.saveStorage(vo.toPO(po));

            return storageId;
        } else {
            throw new Exception(msgVO.getSysoutMsg());
        }
    }

    @Override
    public List<StorageVo> queryAllStorages() throws Exception{
        return storageDao.queryAllStorages().stream().map(it -> {
            StorageVo vo = it.toVo(new StorageVo());
            return vo;
        }).collect(Collectors.toList());
    }


    /**
     * 若在同一个集群中存在相同的UUID则返回true，否则返回false
     *
     * @param vo
     * @return
     */
    private boolean checkExist(final PvVo vo) {
        final List<PvVo> pvVos = queryAll(vo.getClusterId());
        final long count = pvVos.stream().filter(it -> !it.getVolumeId().equals(vo.getVolumeId())).filter(it -> {
            if (it.getClusterId().equals(vo.getClusterId())) {
                return it.getVolumeUuid().equals(vo.getVolumeUuid()) || (!PvConstants.PV_TYPE_CEPH.equals(it.getVolumeType()) && it.getVolumeStoragePath().equals(vo.getVolumeStoragePath()));
            } else {
                return false;
            }
        }).count();
        return count > 0;
    }

    @Override
    public List<DiskVo> checkStorages(StorageVo vo) throws Exception {
        return DiskUtil.getServerDiskMount(vo);
    }

    @Override
    public void deleteStorage(StorageVo vo) throws Exception {
        NfsInstall.removeMountPath(vo);
        storageDao.deleteStorage(vo.getStorageId());
    }

    @Override
    public void deleteStorageForce(StorageVo vo) {
        storageDao.deleteStorage(vo.getStorageId());
    }

    @Override
    public boolean queryByPoolId(Long poolId) {
        return pvDao.queryCountByPoolId(poolId);
    }

    @Override
    public void stopNfs(StorageVo vo) throws Exception{
        try {
            NfsInstall.stopNfs(vo);
        } catch (Exception e) {
            throw new Exception("停止节点失败！");
        }
        if (NfsInstall.activeNfs(vo)){
            throw new Exception("停止节点失败！");
        }
    }

    @Override
    public void startNfs(StorageVo vo) throws Exception{
        try {
            NfsInstall.restartNfs(vo);
        } catch (Exception e) {
            throw new Exception("启动节点失败！");
        }
        if (!NfsInstall.activeNfs(vo)){
            throw new Exception("启动节点失败！");
        }
    }

    @Override
    public StorageVo getStorageById(Long id) {
        StoragePo po = storageDao.queryStorageById(id);
        return po == null?null:po.toVo(new StorageVo());
    }

    @Override
    public List<PvVo> getPvByAppId(Long id) {
        StringBuilder hql = new StringBuilder("select new com.yinhai.cloud.module.resource.pv.api.vo.PvVo(a.volumeId,a.volumeDisplayName,a.volumeUuid,a.volumeEnableState,a.volumeType,a.volumeCloudStorageId,a.volumeStoragePath,a.volumeMaxSpace,a.volumeCreateUser,a.volumeCreateDate,a.clusterId,a.volumeAvailableSpace,a.cephPoolId) from ");
        hql.append("PvPo a ,");
        hql.append("AppPVPo b where a.volumeId = b.volumeId and ");
        hql.append("b.appId=" + id);
        Query query = sessionFactory.getCurrentSession().createQuery(hql.toString());
        if (query.list().isEmpty()){
            return new ArrayList<>();
        }
        return query.list();
    }
}
