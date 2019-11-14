package com.yinhai.cloud.module.application.impl.bpo;


import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.application.api.bpo.IAppConfigBpo;
import com.yinhai.cloud.module.application.api.bpo.IAppVersionBpo;
import com.yinhai.cloud.module.application.api.constant.IAppConstants;
import com.yinhai.cloud.module.application.api.util.AppUtil;
import com.yinhai.cloud.module.application.api.util.AppVersionUtil;
import com.yinhai.cloud.module.application.api.vo.AppConfigVo;
import com.yinhai.cloud.module.application.api.vo.AppVersionVo;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.application.impl.dao.AppVersionDao;
import com.yinhai.cloud.module.application.impl.po.AppVersionPo;
import com.yinhai.cloud.module.repertory.api.bpo.IAppImageBpo;
import com.yinhai.cloud.module.repertory.api.vo.AppImageVo;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liuyi02 on 2019/10/28.
 */
public class AppVersionBpoImpl extends TaBaseBpo implements IAppVersionBpo {
    @Resource
    private AppVersionDao appVersionDao;

    @Resource
    private IAppImageBpo appImageBpo;

    @Resource
    private SessionFactory sessionFactory;

    @Resource
    private IAppConfigBpo appConfigBpo;

    @Resource
    private IAppBpo appBpo;

    @Override
    public List<AppVersionVo> getAllByAppId(Long id) {
        List<AppVersionPo> list = appVersionDao.getAllByAppId(id);
        AppVo appVo = appBpo.getApp(id);
        return list==null?new ArrayList<AppVersionVo>():list.stream().map(po -> {
            AppVersionVo vo = po.toVo(new AppVersionVo());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            vo.setCreateTimeFom(sdf.format(vo.getCreateTime()));
            vo.setVersionNameStr("v" + vo.getVersionName());
            vo.setAppName(appVo.getAppIdentify());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateVersion(AppVersionVo version) {
        AppVersionPo po = appVersionDao.getVersion(version.getId());
        if (version.getIsuse() != null){
            po.setIsuse(version.getIsuse());
        }
        if (version.getImageName() != null){
            po.setImageName(version.getImageName());
        }
        if (version.getImageVersion() != null){
            po.setImageVersion(version.getImageVersion());
        }
        if (version.getVersionName() != null){
            po.setVersionName(version.getVersionName());
        }
        if (version.getIsAction() != null){
            po.setIsAction(version.getIsAction());
        }
        appVersionDao.updateVersion(po);
    }

    @Override
    public List<AppVersionVo> getVersionByAppIdAndImage(Long id, Long appImageId) {
        List<AppVersionPo> list = appVersionDao.getVersionByAppIdAndImage(id,appImageId);
        AppVo appVo = appBpo.getApp(id);
        return list==null?new ArrayList<AppVersionVo>():list.stream().map(po -> {
            AppVersionVo vo = po.toVo(new AppVersionVo());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            vo.setCreateTimeFom(sdf.format(vo.getCreateTime()));
            vo.setVersionNameStr("v" + vo.getVersionName());
            vo.setAppName(appVo.getAppIdentify());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public int getMaxVersion(Long id) {
        String sql = "select max(versionName) from AppVersionPo where appId=?";
        Query query = sessionFactory.getCurrentSession().createQuery(sql);
        query.setInteger(0, id.intValue());
        Object o = query.uniqueResult();
        return o == null?0:Integer.parseInt(o.toString());
    }

    @Override
    public AppVersionVo saveVersion(AppVersionVo appVersionVo) {
        appVersionVo.setCreateTime(new Date());
        int maxVersion = getMaxVersion(appVersionVo.getAppId());
        appVersionVo.setVersionName(++maxVersion);
        AppImageVo imageVo = appImageBpo.getAppImage(appVersionVo.getImageId());
        appVersionVo.setImageVersion(imageVo.getVersion());
        AppConfigVo config = appConfigBpo.getAppConfig(appVersionVo.getAppId());
        appVersionVo.setImageName(config.getAppImagePath());
        AppVo appVo = appBpo.getApp(appVersionVo.getAppId());
        appVersionVo.setAppName(appVo.getAppIdentify());
        AppVersionPo po = appVersionDao.saveVersion(appVersionVo.toPO(new AppVersionPo()));
        return po.toVo(new AppVersionVo());
    }

    @Override
    public void setVersionIsUse(AppVersionVo vo) {
        List<AppVersionVo> list = getAllByAppId(vo.getAppId());
        list.forEach(version -> {
            if (version.getId().longValue() == vo.getId().longValue()){
                version.setIsuse(IAppConstants.APPVERSION_IS_USE);
            }else {
                version.setIsuse(IAppConstants.APPVERSION_NO_USE);
            }
            updateVersion(version);
        });
    }

    @Override
    public List<AppVersionVo> getUseVersion(Long appId) {
        List<AppVersionPo> list = appVersionDao.queryUseVersion(appId);
        AppVo appVo = appBpo.getApp(appId);
        return list==null?new ArrayList<AppVersionVo>():list.stream().map(po -> {
            AppVersionVo vo = po.toVo(new AppVersionVo());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            vo.setCreateTimeFom(sdf.format(vo.getCreateTime()));
            vo.setVersionNameStr("v" + vo.getVersionName());
            vo.setAppName(appVo.getAppIdentify());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public AppVersionVo getVersion(Long id) {
        AppVersionPo po = appVersionDao.getVersion(id);
        return po == null?null:po.toVo(new AppVersionVo());
    }

    @Override
    public void deleteVersion(AppVersionVo vo) throws Exception{
        //删除数据库版本信息
        appVersionDao.deleteById(vo.getId());
        //重置流量分布
        try {
            List<AppVersionVo> list = this.getUseVersion(vo.getAppId());
            if (list.isEmpty()){
                throw new Exception("找不到启动版本信息了");
            }
            AppVersionUtil.backVersion(list.get(0));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
