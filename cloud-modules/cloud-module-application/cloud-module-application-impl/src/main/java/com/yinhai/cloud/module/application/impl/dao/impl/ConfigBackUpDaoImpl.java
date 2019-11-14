package com.yinhai.cloud.module.application.impl.dao.impl;

import com.yinhai.cloud.module.application.api.vo.ConfigBackUpVo;
import com.yinhai.cloud.module.application.impl.dao.ConfigBackUpDao;
import com.yinhai.cloud.module.application.impl.po.ConfigBackUpPo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

/**
 * Created by liuyi on 2019/9/25.
 */
public class ConfigBackUpDaoImpl extends HibernateDAO<ConfigBackUpPo> implements ConfigBackUpDao {

    @Override
    public ConfigBackUpVo saveConfig(ConfigBackUpVo configVo) {
        final ConfigBackUpPo po = new ConfigBackUpPo();
        super.save(configVo.toPO(po));
        ConfigBackUpVo saveVo = po.toVo(new ConfigBackUpVo());
        return saveVo;
    }

    @Override
    public List<ConfigBackUpPo> queryAllBackUpByAppId(Long id) {
        String hql = "from ConfigBackUpPo where appId = ?";
        return find(hql,id);
    }

    @Override
    public ConfigBackUpPo queryAllBackUpById(Long id) {
        return super.get(id);
    }

    @Override
    public void deleteBackUp(Long id) {
        super.delete("delete from ConfigBackUpPo where configId = ?",id);
    }

    @Override
    public void updateConfig(ConfigBackUpPo configBackUpPo) {
        super.update(configBackUpPo);
    }

    @Override
    public List<ConfigBackUpPo> queryAllBackUpByIsUse(String s) {
        String hql = "from ConfigBackUpPo where isUse = ?";
        return find(hql,s);
    }
}
