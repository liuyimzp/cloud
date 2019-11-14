package com.yinhai.cloud.core.api.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pengwei on 2018/8/6.
 */
public class RestConnection {

    /**
     * 获取restful返回信息：除集群状态为普通字符串外，其余信息均为json字符串
     *
     * @param url
     * @return
     * @throws Exception
     */
    private static StringBuilder getInfoFromRestUrl(String url) throws Exception {
        URL restUrl = new URL(url);
        HttpURLConnection huc = (HttpURLConnection) restUrl.openConnection();
        // 5秒连接不上，判为超时
        huc.setConnectTimeout(5000);
        huc.setRequestMethod("GET");
        huc.setRequestProperty("Accept", "application/XML");
        huc.connect();
        if (huc.getResponseCode() != 200) {
            throw new RuntimeException("获取连接信息失败，代码值 : " + huc.getResponseCode());
        }

        BufferedReader bReader = new BufferedReader(
                new InputStreamReader(huc.getInputStream(), "UTF-8"));

        // 对数据进行访问
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        // 关闭流
        bReader.close();
        // 关闭链接
        huc.disconnect();

        return stringBuilder;
    }

    /**
     * 获取集群状态
     *
     * @param apiUrl
     * @return
     * @throws Exception
     */
    public static boolean getClusterStatus(String apiUrl) throws Exception {


        return false;
    }

    public static boolean getNodesStatus(String apiUrl) throws Exception {

        return false;
    }
}
