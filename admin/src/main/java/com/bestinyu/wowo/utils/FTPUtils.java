package com.bestinyu.wowo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class FTPUtils {

    public static boolean upload(String host, Integer port, String user, String pwd, String localFile,
                                 String ftpPath) {
        FTPClient client = null;
        try {
            client = createClient(host, port, user, pwd);

            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();

            if (!client.changeWorkingDirectory(ftpPath)) {
                client.changeWorkingDirectory("/");
                String[] ftpPaths = ftpPath.split("/");
                for (String dir : ftpPaths) {
                    if (StringUtils.hasText(dir)) {
                        if (client.makeDirectory(dir)) {
                            client.changeWorkingDirectory(dir);
                        } else {
                            throw new RuntimeException("changeWorkingDirectory failed.");
                        }
                    }
                }
            }

            File local = new File(localFile);
            if (!local.exists() || local.isDirectory()) {
                throw new RuntimeException("local file not exists.");
            }

            boolean result;
            try (InputStream is = new FileInputStream(localFile)) {
                result = client.storeFile(local.getName(), is);
            }

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (client != null) {
                try {
                    client.disconnect();
                } catch (IOException e) {
                }
            }
        }
    }

    private static FTPClient createClient(String host, Integer port, String user, String pwd) {
        FTPClient client = new FTPClient();
        client.setConnectTimeout(10000);
        client.setDefaultTimeout(10000);
        // client.setDataTimeout(10000);
        // client.setControlKeepAliveTimeout(10);
        // client.setControlKeepAliveReplyTimeout(10000);

        try {
            if (port != null) {
                client.connect(host, port);
            } else {
                client.connect(host);
            }
            client.login(user, pwd);

            int reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                client.disconnect();
                throw new RuntimeException("ftp login failed.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return client;
    }
}
