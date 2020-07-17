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
        boolean download = downloadBing(getBingInfo(day1Url));
        log.info("下载结束");
        if (download) {
            GitUtils.commit();
        }
    }

    @Override
    public void downloadEightDay() {
        log.info("开始下载-8天数据");
        boolean download = downloadBing(getBingInfo(day8Url));
        log.info("下载结束-8天数据");
        GitUtils.commit();
        if (download) {
            GitUtils.commit();
        }
    }

    private BingInfo getBingInfo(String url) {
        BingInfo bingInfo = restTemplate.getForObject(url, BingInfo.class);
        if (bingInfo == null) {
            log.error("Fail to get response from bing.com");
            throw new RuntimeException("Fail to get response from bing.com");
        }

        return bingInfo;
    }

    private boolean downloadBing(BingInfo bingInfo) {
        List<DownloadInfo> downloadInfoList = bingInfo.getImages()
                .stream()
                .map(DownloadInfo::new)
                .collect(Collectors.toList());

        boolean download = false;
        LocalDate today = LocalDate.now();
        for (DownloadInfo info : downloadInfoList) {
            if (info.getImageUrl() != null) {
                download = download || innerDownload(targetDir + imageDir + today + ".jpg", info.getImageUrl());
            }
            if (info.getMp4Url() != null) {
                download = download || innerDownload(targetDir + videoDir + today + ".mp4", info.getMp4Url());
            }
            if (info.getMp4HdUrl() != null) {
                download = download || innerDownload(targetDir + videoHdDir + today + "_hd.mp4", info.getMp4HdUrl());
            }
            if (info.getMp4MobileUrl() != null) {
                download = download || innerDownload(targetDir + videoMobileDir + today + "_m.mp4", info.getMp4MobileUrl());
            }
        }
        return download;
    }

    private boolean innerDownload(String targetPath, String url) {
        log.info("Downloading: {} --> {}", url, targetPath);
        File file = new File(targetPath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                byte[] response = restTemplate.getForObject(url, byte[].class);
                in = new ByteArrayInputStream(response);
                out = new FileOutputStream(file);

                int len;
                byte[] buf = new byte[1024];
                while ((len = in.read(buf, 0, 1024)) != -1) {
                    out.write(buf, 0, len);
                }
            } catch (IOException e) {
                log.error("Download Fail: {}", targetPath, e);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                }
            }
            return true;
        } else {
            log.info("File exists, skip download...");
            return false;
        }
    }
}
