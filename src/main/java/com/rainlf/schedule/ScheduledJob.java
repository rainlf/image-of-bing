package com.rainlf.schedule;

import com.rainlf.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ScheduledJob {

    @Autowired
    private ImageService imageService;

    /**
     * 秒 分 时 日 月 星期
     */
    @Scheduled(cron = "0 0 4 * * *")
    public void spider1Day() {
        imageService.download1Day();
        imageService.runAutoCommit();
    }
}
