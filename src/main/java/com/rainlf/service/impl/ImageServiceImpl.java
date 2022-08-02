package com.rainlf.service.impl;

import com.rainlf.model.AppArguments;
import com.rainlf.model.BingResponse;
import com.rainlf.model.UrlInfo;
import com.rainlf.service.ImageService;
import jdk.internal.util.xml.impl.Input;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.rainlf.model.AppArguments.*;

/**
 * @author : rain
 * @date : 2020/7/10 13:55
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void download1Day() {
        log.info("开始下载");
        download(getBingResponse(day1Url));
        log.info("下载结束");
    }

    @Override
    public void download8Day() {
        log.info("开始下载-8天数据");
        download(getBingResponse(day8Url));
        log.info("下载结束-8天数据");
    }

    private BingResponse getBingResponse(String url) {
        BingResponse bingResponse = restTemplate.getForObject(url, BingResponse.class);
        if (bingResponse == null) {
            throw new RuntimeException("Fail to get response from bing.com");
        }

        return bingResponse;
    }

    private void download(BingResponse bingResponse) {
        List<UrlInfo> urlInfoList = bingResponse.getImages()
                .stream()
                .map(UrlInfo::new)
                .collect(Collectors.toList());

        LocalDate today = LocalDate.now();
        for (int i = 0; i < urlInfoList.size(); i++) {
            UrlInfo info = urlInfoList.get(i);
            LocalDate date = today.minusDays(i);
            if (info.getImageUrl() != null) {
                download(targetDir + imageDir + date + ".jpg", info.getImageUrl());
            }
            if (info.getMp4Url() != null) {
                download(targetDir + videoDir + date + ".mp4", info.getMp4Url());
            }
            if (info.getMp4HdUrl() != null) {
                download(targetDir + videoHdDir + date + "_hd.mp4", info.getMp4HdUrl());
            }
            if (info.getMp4MobileUrl() != null) {
                download(targetDir + videoMobileDir + date + "_m.mp4", info.getMp4MobileUrl());
            }
        }
    }

    private void download(String filePath, String url) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            byte[] response = restTemplate.getForObject(url, byte[].class);
            if (response == null || response.length == 0) {
                log.error("Access {} failed", url);
                return;
            }

            try (
                    InputStream in = new ByteArrayInputStream(response);
                    OutputStream out = Files.newOutputStream(file.toPath());
            ) {
                int len;
                byte[] buf = new byte[1024];
                while ((len = in.read(buf, 0, 1024)) != -1) {
                    out.write(buf, 0, len);
                }
                log.info("Download success: {} <-- {}", filePath, url);
            } catch (IOException e) {
                log.error("Download failed: {} <-- {}", filePath, url, e);
            }
        } else {
            log.info("File exists, skip download: {} <-- {}", filePath, url);
        }
    }

    @Override
    public void runAutoCommit() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(targetScript);
        try {
            Process process = processBuilder.start();
            InputStream processStdout = process.getInputStream();
            InputStream processStderr = process.getErrorStream();
            new Thread(() -> logProcessOutput(processStdout, 1)).start();
            new Thread(() -> logProcessOutput(processStderr, 2)).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * print process output into log
     *
     * @param inputStream stdout or stderr
     * @param logLevel    1: info, 2: error
     */
    private void logProcessOutput(InputStream inputStream, int logLevel) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                printLog(line, logLevel);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printLog(String content, int level) {
        if (level == 1) {
            log.info(content);
        } else {
            log.error(content);
        }
    }
}
