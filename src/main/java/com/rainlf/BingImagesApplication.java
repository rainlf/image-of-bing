package com.rainlf;

import com.rainlf.model.AppArguments;
import com.rainlf.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : rain
 * @date : 2020/7/10 13:37
 */
@SpringBootApplication
public class BingImagesApplication implements CommandLineRunner {

    public static void main(String[] args) {
        parseArgs(args);
        SpringApplication.run(BingImagesApplication.class, args);
    }

    private static void parseArgs(String[] args) {
        if (args.length > 1) {
            AppArguments.targetDir = args[0];
        }
        if (args.length > 2) {
            AppArguments.targetScript = args[1];
        }
    }

    @Autowired
    private ImageService imageService;

    @Override
    public void run(String... args) throws Exception {
        imageService.download8Day();
        imageService.runAutoCommit();
    }
}
