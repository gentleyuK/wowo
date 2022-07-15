package com.bestinyu.wowo.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class CmdUtils {

    public static int linuxExec(String cmd) {
        return exec("sh", "-c", cmd);
    }

    public static int windowsExec(String cmd) {
        return exec("cmd", "/C", cmd);
    }

    public static int exec(String... cmd) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
            int result = p.waitFor();
            log.info("exec cmd:{}, result:{}", cmd, result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }

    private static void info(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                log.info(line);
            }
        }
    }

    private static void error(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                log.error(line);
            }
        }
    }
}
