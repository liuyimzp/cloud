package com.yinhai.cloud.module.application.impl.dao;

import com.yinhai.cloud.module.application.api.vo.ConfigBackUpVo;
import com.yinhai.cloud.module.application.impl.po.ConfigBackUpPo;

import java.util.List;

/**
 * Created by liuyi02 on 2019/9/25.
 */
public interface ConfigBackUpDao {

    ConfigBackUpVo saveConfig(ConfigBackUpVo configVo);

    /**
     * 查询应用所有备份
     * @param id
     * @return
     */
    List<ConfigBackUpPo> queryAllBackUpByAppId(Long id);

    /**
     * 查询某一备份信息
     * @param id
     * @return
     */
    ConfigBackUpPo queryAllBackUpById(Long id);

    /**
     * 删除备份信息
     * @param po
     */
    void deleteBackUp(Long id);

    /**
     * 修改数据库中备份相关数据
     * @param configBackUpPo
     */
    void updateConfig(ConfigBackUpPo configBackUpPo);

    /**
     * 根据备份信息是否被使用查询数据
     * @param s
     * @return
     */
    List<ConfigBackUpPo> queryAllBackUpByIsUse(String s);
}
