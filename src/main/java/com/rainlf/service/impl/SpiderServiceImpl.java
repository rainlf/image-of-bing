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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : rain
 * @date : 2020/7/10 13:55
 */
@Slf4j
@Service
public class SpiderServiceImpl implements SpiderService {

    private final String imageDir = "/store/image/";
    private final String videoDir = "/store/video/";
    private final String videoHdDir = "/store/video_hd/";
    private final String videoMobileDir = "/store/video_mobile";
    private final String day1Url = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&video=1";
    private final String day8Url = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=8&video=1";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void downloadOneDay(String targetDir) {
        long s = System.currentTimeMillis();
        downloadBing(getBingInfo(day1Url), targetDir);
        log.info("rain: {}", System.currentTimeMillis() - s);
        s = System.currentTimeMillis();
        GitUtils.commit();
        log.info("rain2: {}", System.currentTimeMillis() - s);
    }

    @Override
    public void downloadEightDay(String targetDir) {
        downloadBing(getBingInfo(day8Url), targetDir);
    }

    private BingInfo getBingInfo(String url) {
        BingInfo bingInfo = restTemplate.getForObject(url, BingInfo.class);
        if (bingInfo == null) {
            log.error("Fail to get response from bing.com");
            throw new RuntimeException("Fail to get response from bing.com");
        }

        return bingInfo;
    }

    private void downloadBing(BingInfo bingInfo, String targetDir) {
        List<DownloadInfo> downloadInfoList = bingInfo.getImages()
                .stream()
                .map(DownloadInfo::new)
                .collect(Collectors.toList());

        for (DownloadInfo info : downloadInfoList) {
            if (info.getImageName() != null) {
                innerDownload(targetDir + imageDir + info.getImageName(), info.getImageUrl());
            }
            if (info.getMp4Name() != null) {
                innerDownload(targetDir + videoDir + info.getMp4Name(), info.getMp4Url());
            }
            if (info.getMp4HdName() != null) {
                innerDownload(targetDir + videoHdDir + info.getMp4HdName(), info.getMp4HdUrl());
            }
            if (info.getMp4MobileName() != null) {
                innerDownload(targetDir + videoMobileDir + info.getMp4MobileName(), info.getMp4MobileUrl());
            }
        }
    }

    private void innerDownload(String targetPath, String url) {
        File file = new File(targetPath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            byte[] response = restTemplate.getForObject(url, byte[].class);
            try (InputStream in = new ByteArrayInputStream(response);
                 OutputStream out = new FileOutputStream(file)) {
                int len;
                byte[] buf = new byte[1024];
                while ((len = in.read(buf, 0, 1024)) != -1) {
                    out.write(buf, 0, len);
                }

            } catch (IOException e) {
               log.error("Download Fail: {}", targetPath, e);
            }
        } else {
            log.info("File exists, skip download...");
        }
    }
}
