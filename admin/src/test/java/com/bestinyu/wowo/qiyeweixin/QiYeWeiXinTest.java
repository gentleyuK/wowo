package com.bestinyu.wowo.qiyeweixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class QiYeWeiXinTest {

    private static String corpid = "wwf1bd76ce585a1416";
    private static int agentid = 3010040;
    private static String corpsecret = "9Oas6NjZqVWmS5nsTxDwvlnCKVTMXi2hL2KW6Mf12ts";
    private static String accessToken = "jAM2NyphAhVjWEfofy9T5VDfsSJYxqRPJU2YeBPZS_NkFqhI5y7xbeqCeS716JTw-yqNKStV" +
            "RCnPnZUQ8VmEOmh4qpi2SEyOBC6Ks-t3tqEhb72QIsUdpR0m6LBaths_c3cg4MGQPNKQcWvhYUCKU5Cs4YXVMaUD3ubX-Rb5suFA" +
            "X3yqRxvVWPLKGtn8IoBGLdnQtTDG_H-4zLZjGQHgJQ";

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
    }

    // 2021-12-16 15:36
    // {"errcode":0,"errmsg":"ok","access_token":"X8nDZRuuAmOcauKeiuiA7yRiUfG3naxvte48QeXnAljquwCVUWLbZyFvMlxQM1K
    // YR3nJv43YG5OpsBusc6BEEl4r0pabv3FKZIcw9vE2nMdL1RXycaFx6DyaDKVn_j6rirIqOYNljNokJrUCGU9YJHaLEbf3A3sIY9bRKUxOc
    // vjzd6NgdMhFdG8iDq_hMWYqnPlfVr23LXW7nxNsm6Vc9g","expires_in":7200}
    // 2021-12-16 17:21
    @Test
    public void getAccessToken() {
        String accessTokenUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret;
        String accessTokenInfo = sendRequest(accessTokenUrl, "GET", null);
        System.out.println(accessTokenInfo);
    }

    @Test
    public void getDepartment() throws IOException {
        String departmentUrl = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=" + accessToken;
        String responseStr = sendRequest(departmentUrl, "GET", null);

        JSONObject response = JSON.parseObject(responseStr);

        JSONArray departments = response.getJSONArray("department");

        List<Department> departmentList = new ArrayList<>(departments.size());
        for (Object departmentJson : departments) {
            long departmentId = ((JSONObject) departmentJson).getLong("id");
            String departmentName = ((JSONObject) departmentJson).getString("name");

            Department department = new Department(departmentId, departmentName);

            List<User> userList = new ArrayList<>();
        }
    }

    // "errcode":42001,"errmsg":"access_token expired, h
    @Test
    public void getUsers() {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=" + accessToken +
                "&department_id=" + 1 + "&fetch_child=1";
        String usersInfo = sendRequest(url, "GET", null);
        System.out.println(usersInfo);
    }

    // "errcode":42001,"errmsg":"access_token expired, hint
    @Test
    public void sendMsg() {
        String sendMsgUrl = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;

        WeiXinMsgTO msgTO = new WeiXinMsgTO();
        // private String touser;
        msgTO.setTouser("crucible");
        // private String toparty;
        // private String totag;
        // private int agentid;
        msgTO.setAgentid(agentid);
        // private Text text;
        msgTO.setTextContent("审计告警发送测试消息");

        String response = sendRequest(sendMsgUrl, "POST", JSON.toJSONString(msgTO));
        System.out.println(response);
    }

    private static String sendRequest(String url, String method, String requestBody) {
        HttpURLConnection connection = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            URL connectionUrl = new URL(url);
            connection = (HttpURLConnection) connectionUrl.openConnection();

            if ("https".equals(connectionUrl.toURI().getScheme())) {
                ignoreSSLVerify((HttpsURLConnection) connection);
            }

            connection.setRequestMethod(method);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "*/*");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.connect();

            if (StringUtils.hasText(requestBody)) {
                os = connection.getOutputStream();
                os.write(requestBody.getBytes("UTF-8"));
                os.flush();
            }

            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
            } else {
                is = connection.getErrorStream();
            }

            byte[] res = readInputStream(is);
            return new String(res, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeResource(is);
            closeResource(os);
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static void ignoreSSLVerify(HttpsURLConnection connection) throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustManagers, new SecureRandom());

        connection.setSSLSocketFactory(sslContext.getSocketFactory());
        connection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
    }

    private static byte[] readInputStream(InputStream is) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024 * 1024 * 1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            return os.toByteArray();
        }
    }

    private static void closeResource(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }
}
