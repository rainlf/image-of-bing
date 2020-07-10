package com.rainlf;

import com.rainlf.config.ScheduledConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : rain
 * @date : 2020/7/10 13:37
 */
@SpringBootApplication
public class BingBackgroundApplication {

    public static void main(String[] args) {
        parseArgs(args);
        SpringApplication.run(BingBackgroundApplication.class, args);
    }

    private static void parseArgs(String[] args) {
        if (args.length == 0) {
            ScheduledConfig.targetDir = "./";
        } else {
            ScheduledConfig.targetDir = args[0];
        }
    }

}
