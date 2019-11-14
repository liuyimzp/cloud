package com.yinhai.cloud.module.user.api.vo;

import com.yinhai.core.common.api.util.ValidateUtil;
import com.yinhai.core.common.ta3.vo.BaseVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianhy on 2018/9/17.
 */
public class UserAuthorityListVo extends BaseVo {

    private static final long serialVersionUID = -3477206317617901835L;

    private Long userId;

    private List<UserAuthorityVo> addList;

    private List<UserAuthorityVo> removeList;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<UserAuthorityVo> getAddList() {
        if (addList != null){
            return new ArrayList<>(addList);
        }
        return null;
    }

    public void setAddList(List<UserAuthorityVo> addList) {
        if (addList != null){
            this.addList = new ArrayList<>(addList);
        }
    }

    public List<UserAuthorityVo> getRemoveList() {
        if (removeList != null){
            return new ArrayList<>(removeList);
        }
        return null;
    }

    public void setRemoveList(List<UserAuthorityVo> removeList) {
        if (removeList != null){
            this.removeList = new ArrayList<>(removeList);
        }
    }
}
