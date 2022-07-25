package com.bestinyu.wowo;

import com.bestinyu.wowo.utils.EMailUtils;
import com.bestinyu.wowo.utils.FTPUtils;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Test {

    public static void main(String[] args) throws UnsupportedEncodingException, MessagingException {
        String host = "smtp.exmail.qq.com";
        String port = "465";
        String send = "baoyu@secsmart.net";
        String sendPwd = "111111Qwert";
        String[] rec = new String[]{"baoyu@secsmart.net"};
        EMailUtils.send(host, port, "stmp", send, sendPwd, rec, null, "测试", null, null, "测试");
    }

    @org.junit.Test
    public void test() {
        long start = System.currentTimeMillis();
        try {
            FTPUtils.upload(
                    "172.16.194.49", 21, "ftpuser", "1",
                    "C:/Users/Bestinyu/Desktop/test.gz", "/jiahong/r200"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    private static void print(InputStream is) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
