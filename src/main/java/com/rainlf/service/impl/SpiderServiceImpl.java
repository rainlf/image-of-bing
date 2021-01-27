package com.rainlf.service.impl;

import com.rainlf.domain.BingInfo;
import com.rainlf.domain.DownloadInfo;
import com.rainlf.service.SpiderService;
import com.rainlf.util.GitUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : rain
 * @date : 2020/7/10 13:55
 */
@Slf4j
@Service
public class SpiderServiceImpl implements SpiderService {

    public static String targetDir = "";

    private final String imageDir = "/store/image/";
    private final String videoDir = "/store/video/";
    private final String videoHdDir = "/store/video_hd/";
    private final String videoMobileDir = "/store/video_mobile";
    private final String day1Url = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&video=1";
    private final String day8Url = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=8&video=1";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void downloadOneDay() {
        log.info("开始下载");
        downloadBing(getBingInfo(day1Url));
        log.info("下载结束");
    }

    @Override
    public void downloadEightDay() {
        log.info("开始下载-8天数据");
        downloadBing(getBingInfo(day8Url));
        log.info("下载结束-8天数据");
    }

    private BingInfo getBingInfo(String url) {
        BingInfo bingInfo = restTemplate.getForObject(url, BingInfo.class);
        if (bingInfo == null) {
            log.error("Fail to get response from bing.com");
            throw new RuntimeException("Fail to get response from bing.com");
        }

        return bingInfo;
    }

    private void downloadBing(BingInfo bingInfo) {
        List<DownloadInfo> downloadInfoList = bingInfo.getImages()
                .stream()
                .map(DownloadInfo::new)
                .collect(Collectors.toList());

        LocalDate today = LocalDate.now();
        for (int i = 0; i < downloadInfoList.size(); i++) {
            DownloadInfo info = downloadInfoList.get(i);
            LocalDate date = today.minusDays(i);
            if (info.getImageUrl() != null) {
                innerDownload(targetDir + imageDir + date + ".jpg", info.getImageUrl());
            }
            if (info.getMp4Url() != null) {
                innerDownload(targetDir + videoDir + date + ".mp4", info.getMp4Url());
            }
            if (info.getMp4HdUrl() != null) {
                innerDownload(targetDir + videoHdDir + date + "_hd.mp4", info.getMp4HdUrl());
            }
            if (info.getMp4MobileUrl() != null) {
                innerDownload(targetDir + videoMobileDir + date + "_m.mp4", info.getMp4MobileUrl());
            }
        }
    }

    private boolean innerDownload(String filePath, String url) {
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

            try (InputStream in = new ByteArrayInputStream(response);
                 OutputStream out = new FileOutputStream(file)) {
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
