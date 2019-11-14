package com.yinhai.cloud.module.resource.cluster.impl.dao.impl;

import com.yinhai.cloud.module.resource.cluster.impl.dao.ImageArrDao;
import com.yinhai.cloud.module.resource.cluster.impl.po.ImageArrVo;
import com.yinhai.core.service.ta3.domain.dao.HibernateDAO;

import java.util.List;

public class ImageArrDaoImpl extends HibernateDAO<ImageArrVo> implements ImageArrDao {
    @Override
    public List<ImageArrVo> getAllAppImages() {
        return super.find("from " + super.getEntityClassName(ImageArrVo.class.getName()) + " ORDER BY createTime DESC");
    }
}
