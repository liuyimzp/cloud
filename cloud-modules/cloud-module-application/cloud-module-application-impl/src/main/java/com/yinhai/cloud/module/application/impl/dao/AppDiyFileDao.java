package com.yinhai.cloud.module.application.impl.dao;

import com.yinhai.cloud.module.application.impl.po.AppDiyFileVo;

import java.util.List;

/**
 * Created by tianhy on 2018/6/12.
 */
public interface AppDiyFileDao {

    boolean saveFile(Long id,String appDiyFile,String appStatus);
}
