package com.rainlf.service.impl;

import com.rainlf.model.BingResponse;
import com.rainlf.model.UrlInfo;
import com.rainlf.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : rain
 * @date : 2020/7/10 13:55
 */
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    public static String targetDir = "./store/";

    private final String imageDir = "/image/";
    private final String videoDir = "/video/";
    private final String videoHdDir = "/video_hd/";
    private final String videoMobileDir = "/video_mobile";
    private final String day1Url = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&video=1";
    private final String day8Url = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=8&video=1";

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

    private boolean download(String filePath, String url) {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            byte[] response = restTemplate.getForObject(url, byte[].class);
            if (response == null || response.length == 0) {
                log.error("Access {} failed", url);
                return false;
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
            return true;
        } else {
            log.info("File exists, skip download: {} <-- {}", filePath, url);
            return false;
        }
    }
}
