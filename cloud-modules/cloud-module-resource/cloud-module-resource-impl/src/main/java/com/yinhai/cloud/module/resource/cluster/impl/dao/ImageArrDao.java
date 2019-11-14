package com.yinhai.cloud.module.resource.cluster.impl.dao;

import com.yinhai.cloud.module.resource.cluster.impl.po.ImageArrVo;

import java.util.List;

/**
 * @author zhaokai
 */
public interface ImageArrDao {
    List<ImageArrVo> getAllAppImages();
}
