package com.rainlf.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author : rain
 * @date : 2020/7/10 16:06
 */
@Slf4j
public class GitUtils {

    public static void commit() {
        log.info("[Git] start commit");
        String message = System.getProperty("os.name").toLowerCase().contains("win") ? "update" : "\uD83C\uDF56";
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "git add . && git commit -m " + message + " && git pull --rebase && git push");
        processBuilder.redirectErrorStream(true);
        Process process = null;
        try {
            process = processBuilder.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            new Thread(() -> {
                try {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        log.info("[Git] {}", line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            log.error("[Git] commit error", e);
        } finally {
            log.info("[Git] end commit");
            if (process != null) {
                process.destroy();
            }
        }
    }

}
