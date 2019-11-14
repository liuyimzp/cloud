package com.yinhai.cloud.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域安全：给response增加允许跨域的头
 * @author jianglw
 */
public class AllowCorsFilter implements Filter {
    @Override
    public void init(final FilterConfig filterConfig) {
        return;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(request, resp);
    }

    @Override
    public void destroy() {
        return;
    }
}
