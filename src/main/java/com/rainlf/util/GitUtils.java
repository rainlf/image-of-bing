package com.rainlf.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author : rain
 * @date : 2020/7/10 16:06
 */
@Slf4j
public class GitUtils {

    @SneakyThrows
    public static void commit() {
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", "git", "commit", "-m", "update", "&&", "git", "push");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            log.info("[Git] {}", line);
        }

        process.destroy();
    }
}
