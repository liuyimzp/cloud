package com.yinhai.cloud.module.application.impl.bpo;

import com.yinhai.cloud.module.application.api.bpo.IAppBpo;
import com.yinhai.cloud.module.application.api.bpo.IConfigBackUpBpo;
import com.yinhai.cloud.module.application.api.util.AppUtil;
import com.yinhai.cloud.module.application.api.vo.AppVo;
import com.yinhai.cloud.module.application.api.vo.ConfigBackUpVo;
import com.yinhai.cloud.module.application.impl.dao.ConfigBackUpDao;
import com.yinhai.cloud.module.application.impl.po.ConfigBackUpPo;
import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.service.ta3.domain.bpo.TaBaseBpo;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by liuyi02 on 2019/9/25.
 */
public class ConfigBackUpBpoImpl extends TaBaseBpo implements IConfigBackUpBpo {

    @Resource
    private ConfigBackUpDao configBackUpDao;

    @Resource
    private IAppBpo appBpo;

    @Override
    public void saveConfig(ConfigBackUpVo configVo) {
        configBackUpDao.saveConfig(configVo);
    }

    @Override
    public List<ConfigBackUpVo> queryAllBackUpByAppId(Long id) {
        List<ConfigBackUpPo> configBackUpPos = configBackUpDao.queryAllBackUpByAppId(id);
        return ValidateUtil.isEmpty(configBackUpPos)?new ArrayList<>():configBackUpPos.stream().map(cpo -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ConfigBackUpVo configBackUpVo = cpo.toVo(new ConfigBackUpVo());
            AppVo appVo = appBpo.getApp(id);
            configBackUpVo.setCreateTimeFom(sdf.format(configBackUpVo.getCreateTime()));
            configBackUpVo.setAppName(appVo.getAppIdentify());
            return configBackUpVo;
        }).collect(Collectors.toList());
    }

    @Override
    public ConfigBackUpVo queryAllBackUpById(Long id) {
        ConfigBackUpPo configBackUpPo = configBackUpDao.queryAllBackUpById(id);
        if (configBackUpPo == null){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ConfigBackUpVo configBackUpVo = configBackUpPo.toVo(new ConfigBackUpVo());
        AppVo appVo = appBpo.getApp(configBackUpPo.getAppId());
        configBackUpVo.setCreateTimeFom(sdf.format(configBackUpVo.getCreateTime()));
        configBackUpVo.setAppName(appVo.getAppIdentify());
        return configBackUpVo;
    }

    @Override
    public void deleteConfigBackUp(ConfigBackUpVo configBackUpVo) throws Exception{
        try {
            //将备份信息删除
            AppUtil.deleteBackUpFile(configBackUpVo);
            //降档请信息删除
            configBackUpDao.deleteBackUp(configBackUpVo.getConfigId());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void reductionConfigBackUp(ConfigBackUpVo configBackUpVo) throws Exception{
        try {
            //将备份文件覆盖掉以前的
            AppUtil.reductionConfigBackUp(configBackUpVo);
            //将之前使用的备份文件设置为未使用
            List<ConfigBackUpPo> configBackUpPos = configBackUpDao.queryAllBackUpByIsUse("1");
            if (!configBackUpPos.isEmpty()){
                for (ConfigBackUpPo po : configBackUpPos){
                    po.setIsUse("0");
                    configBackUpDao.updateConfig(po);
                }
            }
            //将当前这条数据修改为正在使用
            configBackUpVo.setIsUse("1");
            updateConfig(configBackUpVo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void updateConfig(ConfigBackUpVo configBackUpVo){
        ConfigBackUpPo configBackUpPo = configBackUpDao.queryAllBackUpById(configBackUpVo.getConfigId());
        if (ValidateUtil.isNotEmpty(configBackUpVo.getIsUse())){
            configBackUpPo.setIsUse(configBackUpVo.getIsUse());
        }
        if (ValidateUtil.isNotEmpty(configBackUpVo.getIsConfig())){
            configBackUpPo.setIsConfig(configBackUpVo.getIsConfig());
        }
        if (ValidateUtil.isNotEmpty(configBackUpVo.getBackup_file_path())){
            configBackUpPo.setBackup_file_path(configBackUpVo.getBackup_file_path());
        }
        if (ValidateUtil.isNotEmpty(configBackUpVo.getBackup_ismount())){
            configBackUpPo.setBackup_ismount(configBackUpVo.getBackup_ismount());
        }
        configBackUpDao.updateConfig(configBackUpPo);
    }

    @Override
    public void deleteByAppId(Long id) throws Exception{
        List<ConfigBackUpVo> vos = queryAllBackUpByAppId(id);
        if (!vos.isEmpty()){
            for (ConfigBackUpVo vo : vos){
                try {
                    deleteConfigBackUp(vo);
                } catch (Exception e) {
                    throw new Exception(e.getMessage());
                }
            }
        }
    }
}
