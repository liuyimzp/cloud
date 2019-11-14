package com.yinhai.cloud.module.repertory.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

/**
 * Created by tianhy on 2018/7/30.
 * 应用分组数据库对象
 */
public class AppGroupPo extends BasePo {
    private static final long serialVersionUID = -4901448586292281864L;

    // 序列号（主键）
    private Long id;
    // 应用分组标识
    private String groupIdentify;
    // 应用分组名称
    private String groupName;
    // 所属业务领域
    private String businessArea;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupIdentify() {
        return groupIdentify;
    }

    public void setGroupIdentify(String groupIdentify) {
        this.groupIdentify = groupIdentify;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }
}
