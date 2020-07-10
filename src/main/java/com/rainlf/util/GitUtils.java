package com.rainlf.util;

import lombok.SneakyThrows;

/**
 * @author : rain
 * @date : 2020/7/10 16:06
 */
public class GitUtils {

    @SneakyThrows
    public static void commit() {
        ProcessBuilder pb1 = new ProcessBuilder("git", "commit", "-am", "update");
        Process process = pb1.start();
        if (process.exitValue() == 0) {
            ProcessBuilder pb2 = new ProcessBuilder("git push");
            pb2.start();
        }
    }
}
