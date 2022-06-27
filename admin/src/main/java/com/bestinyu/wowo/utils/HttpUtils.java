package com.bestinyu.wowo.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class HttpUtils {

    public static String httpPost(String requestUrl, String charset) {
        return httpRequest("POST", requestUrl, charset);
    }

    public static String httpGet(String requestUrl, String charset) {
        return httpRequest("GET", requestUrl, charset);
    }

    public static String httpRequest(String method, String requestUrl, String charset) {
        HttpURLConnection conn = null;
        try {
            // 获取连接
            conn = getConn(requestUrl, method);
            // 发送请求
            conn.connect();
            // 读取响应
            return readResponse(conn, charset);
        } catch (IOException e) {
            log.error("httpGet error", e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static HttpURLConnection getConn(String requestUrl, String method) throws IOException {
        // 创建远程url连接对象
        URL url = new URL(requestUrl);
        // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 设置连接方式：get
        connection.setRequestMethod(method);
        // 设置连接主机服务器的超时时间：15000毫秒
        connection.setConnectTimeout(15000);
        // 设置读取远程返回的数据时间：60000毫秒
        connection.setReadTimeout(60000);

        return connection;
    }

    private static String readResponse(HttpURLConnection connection, String charset) throws IOException {
        if (connection == null) {
            return null;
        }

        BufferedReader br = null;
        try {
            // 通过connection连接，获取输入流
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), charset));
            }

            // 读取响应
            StringBuilder res = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                res.append(temp).append("\n");
            }
            return res.toString();
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }
}
