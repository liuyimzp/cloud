package com.yinhai.cloud.module.repertory.api.vo;

import com.yinhai.core.common.ta3.vo.BaseVo;

/**
 * Created by tianhy on 2018/7/30.
 */
public class AppGroupVo extends BaseVo {
    private static final long serialVersionUID = 380277585398277043L;

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
