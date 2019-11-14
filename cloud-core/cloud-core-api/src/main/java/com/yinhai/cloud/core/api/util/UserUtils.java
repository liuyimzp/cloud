package com.yinhai.cloud.core.api.util;

import com.yinhai.modules.security.api.vo.UserAccountInfo;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * @author jianglw
 */
public class UserUtils {
    private static final String USER_INFO_KEY = "ta3.userinfo";

    /**
     * 获取用户ID
     *
     * @param request
     * @return
     */
    @NotNull
    public static String getUserId(ServletRequest request) {
        String userId = ((UserAccountInfo) ((HttpServletRequest) request).getSession().getAttribute(USER_INFO_KEY)).getUserId();
        if (userId == null) {
            userId = "";
        }
        return userId;
    }

}
