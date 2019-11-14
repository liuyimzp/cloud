package com.yinhai.cloud.controller;

import com.yinhai.cloud.core.api.vo.CommonResultVo;
import com.yinhai.cloud.module.resource.constants.ResourceConstant;
import com.yinhai.cloud.vo.UserVo;
import com.yinhai.core.app.api.util.JSonFactory;
import com.yinhai.modules.security.api.vo.UserAccountInfo;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jianglw
 */
@Controller
@RequestMapping(value = "/user")
public class UserLoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginController.class);
    
    @RequestMapping("/login.do")
    @ResponseBody
    public CommonResultVo login(HttpServletRequest req, HttpServletResponse resp, @RequestBody UserVo vo) {
        final CommonResultVo resultVo = new CommonResultVo();
        final String login = req.getRequestURL().toString().replaceFirst("/user/login.do", "/j_spring_security_check");
        final HttpUriRequest post = RequestBuilder.post(login).addParameter("username", vo.getUserName()).addParameter("password", vo.getPassword()).addParameter("checkCode", "11").build();
        try (final CloseableHttpClient client = HttpClients.custom().build()) {
            CloseableHttpResponse mockResp = client.execute(post);
            Header[] mockRespHeader = mockResp.getAllHeaders();
            for (Header header : mockRespHeader) {
                if ("Set-Cookie".equals(header.getName())) {
                    if (header.getValue() != null && header.getValue().contains("JSESSIONID")) {
                        resp.setHeader("Set-Cookie", header.getValue());
                    } else {
                        resp.addHeader(header.getName(), header.getValue());
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.debug(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            resultVo.setSuccess(false);
            resultVo.setErrorMsg(e.getMessage());
            return resultVo;
        }
        
        resultVo.setSuccess(true);
        vo.setPassword("");
        resultVo.setData(vo);
        return resultVo;
    }
    
    @RequestMapping("/logout.do")
    @ResponseBody
    public CommonResultVo logout(HttpServletRequest request) {
        final CommonResultVo resultVo = new CommonResultVo();
        final HttpSession session = request.getSession(false);
        if (session != null) {
            final Enumeration<String> attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                session.removeAttribute(attributeNames.nextElement());
            }
        }
        resultVo.setSuccess(true);
        return resultVo;
    }
    
    @RequestMapping("/changePwd.do")
    public String changePwd() {
        return null;
    }
    
    @RequestMapping("/getUser.do")
    @ResponseBody
    public CommonResultVo getLoginUserInfo(HttpServletRequest request) {
        final CommonResultVo vo = new CommonResultVo();
        final Object attribute = request.getSession(false).getAttribute(ResourceConstant.USER_INFO_KEY);
        if (attribute != null) {
            vo.setSuccess(true);
            final UserAccountInfo userAccountInfo = (UserAccountInfo) attribute;
            Map map = new HashMap<>();
            map.put("name", userAccountInfo.getName());
            map.put("positionId", userAccountInfo.getNowPosition().getPositionid());
            vo.setData(map);
        } else {
            vo.setSuccess(false);
            vo.setErrorMsg("当前未登录任何用户");
        }
        return vo;
    }
}
