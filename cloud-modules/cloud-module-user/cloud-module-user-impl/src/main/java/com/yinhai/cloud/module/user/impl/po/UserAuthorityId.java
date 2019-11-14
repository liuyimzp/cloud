package com.yinhai.cloud.module.user.impl.po;

import java.io.Serializable;

/**
 * Created by tianhy on 2018/9/11.
 */
public class UserAuthorityId implements Serializable {
    private static final long serialVersionUID = -2121813829198344878L;

    private Long userId;

    private Long resourceId;

    public UserAuthorityId() {
    }

    public UserAuthorityId(Long userId, Long resourceId) {
        this.userId = userId;
        this.resourceId = resourceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }
}
