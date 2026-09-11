package com.swacha.qacloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class QaCloudPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(QaCloudPlatformApplication.class, args);
    }
}
