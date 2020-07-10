package com.rainlf.util;

import lombok.SneakyThrows;

/**
 * @author : rain
 * @date : 2020/7/10 16:06
 */
public class GitUtils {

    @SneakyThrows
    public static void commit() {
        ProcessBuilder processBuilder = new ProcessBuilder("git ci -am 'update'");
        Process process = processBuilder.start();
        System.out.println(1);
    }
}
