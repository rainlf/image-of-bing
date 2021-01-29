package com.rainlf;

import com.rainlf.service.SpiderService;
import com.rainlf.service.impl.SpiderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : rain
 * @date : 2020/7/10 13:37
 */
@SpringBootApplication
public class BingImagesStoreApplication implements CommandLineRunner {

    public static void main(String[] args) {
        parseArgs(args);
        SpringApplication.run(BingImagesStoreApplication.class, args);
    }

    private static void parseArgs(String[] args) {
        if (args.length == 0) {
            SpiderServiceImpl.targetDir = "./";
        } else {
            SpiderServiceImpl.targetDir = args[0];
        }
    }

    @Autowired
    private SpiderService spiderService;

    @Override
    public void run(String... args) throws Exception {
        spiderService.downloadEightDay();
    }
}
