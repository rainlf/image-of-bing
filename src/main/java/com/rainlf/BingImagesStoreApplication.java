package com.rainlf;

import com.rainlf.service.ImageService;
import com.rainlf.service.impl.ImageServiceImpl;
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
            ImageServiceImpl.targetDir = "./store";
        } else {
            ImageServiceImpl.targetDir = args[0];
        }
    }

    @Autowired
    private ImageService imageService;

    @Override
    public void run(String... args) throws Exception {
        imageService.download8Day();
    }
}
