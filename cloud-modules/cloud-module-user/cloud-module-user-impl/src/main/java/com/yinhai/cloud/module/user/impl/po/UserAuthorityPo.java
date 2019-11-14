package com.yinhai.cloud.module.user.impl.po;

import com.yinhai.core.service.ta3.domain.po.BasePo;

/**
 * Created by tianhy on 2018/9/11.
 */
public class UserAuthorityPo extends BasePo {

    private static final long serialVersionUID = -8989388053984623320L;

    private UserAuthorityId id;

    private String resourceType;

    public UserAuthorityPo() {
    }

    public UserAuthorityPo(UserAuthorityId id, String resourceType) {
        this.id = id;
        this.resourceType = resourceType;
    }

    public UserAuthorityId getId() {
        return id;
    }

    public void setId(UserAuthorityId id) {
        this.id = id;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}
