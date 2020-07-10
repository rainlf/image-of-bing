package com.rainlf.config;

import com.rainlf.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledConfig {

    public static String targetDir = "";

    @Autowired
    private SpiderService spiderService;

    /**
     * 秒 分 时 日 月 星期
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void spider1Day() {
//        spiderService.downloadOneDay(targetDir);
        spiderService.downloadEightDay(targetDir);
    }
}
