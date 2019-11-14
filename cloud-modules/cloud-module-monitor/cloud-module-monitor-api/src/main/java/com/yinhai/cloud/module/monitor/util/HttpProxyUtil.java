package com.yinhai.cloud.module.monitor.util;

import com.yinhai.cloud.core.api.util.SystemConfigUtil;
import com.yinhai.cloud.module.monitor.api.vo.ProxyVo;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.apache.curator.shaded.com.google.common.net.HttpHeaders.USER_AGENT;

/**
 * @Author ：kangdw
 * @Date : 2019/8/8
 */
public class HttpProxyUtil {
    /**
     * 代理前端请求到指定地址
     **/
    public static String HttpProxyGet(ProxyVo proxyVo) throws IOException {
        StringBuffer urlBuffer = new StringBuffer();
        urlBuffer.append("http://");
        urlBuffer.append(proxyVo.getMonitorIP());
        urlBuffer.append(":");
        urlBuffer.append(SystemConfigUtil.getSystemConfigValue("prometheus.port"));
        urlBuffer.append("/api/v1/query_range");
        urlBuffer.append("?");
        urlBuffer.append("query=");
        urlBuffer.append(proxyVo.getMonitorParamsVo().getQuery());
        urlBuffer.append("&");
        urlBuffer.append("start=");
        urlBuffer.append(proxyVo.getMonitorParamsVo().getStart());
        urlBuffer.append("&");
        urlBuffer.append("end=");
        urlBuffer.append(proxyVo.getMonitorParamsVo().getEnd());
        urlBuffer.append("&");
        urlBuffer.append("step=");
        urlBuffer.append(proxyVo.getMonitorParamsVo().getStep());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(urlBuffer.toString());

        request.addHeader("User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);
        //请求返回的状态码response.getStatusLine().getStatusCode()
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }


}

