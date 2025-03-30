package com.rainlf;

import com.rainlf.model.AppArguments;
import com.rainlf.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : rain
 * @date : 2020/7/10 13:37
 */
@Slf4j
@SpringBootApplication
public class ImagesOfBingApplication implements CommandLineRunner {

    public static void main(String[] args) {
        parseArgs(args);
        SpringApplication.run(ImagesOfBingApplication.class, args);
    }

    private static void parseArgs(String[] args) {
        String osName = System.getProperty("os.name");
        log.info("osName: " + osName);
        if (osName.contains("Mac")) {
            AppArguments.targetScript = AppArguments.macScript;
        } else if (osName.contains("Windows")) {
            AppArguments.targetScript = AppArguments.windowsScript;
        } else {
            AppArguments.targetScript = AppArguments.linuxScript;
        }
        log.info("targetScript: " + AppArguments.targetScript);

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
