package com.yinhai.cloud.module.application.api.bpo;


import com.yinhai.cloud.module.application.api.vo.ConfigBackUpVo;

import java.util.List;

/**
 * Created by liuyi02 on 2019/9/25.
 */
public interface IConfigBackUpBpo {


    /**
     * 保存备份日志数据
     * @param configVo
     */
    void saveConfig(ConfigBackUpVo configVo);

    /**
     * 获得应用所有备份
     * @param id
     * @return
     */
    List<ConfigBackUpVo> queryAllBackUpByAppId(Long id);

    /**
     * 获得某一个备份信息通过备份信息唯一标识符
     * @param configId
     * @return
     */
    ConfigBackUpVo queryAllBackUpById(Long configId);

    /**
     * 删除该条备份信息
     * @param configBackUpVo
     */
    void deleteConfigBackUp(ConfigBackUpVo configBackUpVo) throws Exception;

    /**
     * 还原备份信息
     * @param vo
     */
    void reductionConfigBackUp(ConfigBackUpVo vo) throws Exception;

    /**
     * 修改备份信息
     * @param vo
     */
    void updateConfig(ConfigBackUpVo vo);

    /**
     *
     * @param id
     */
    void deleteByAppId(Long id) throws Exception;
}
