package com.swacha.qacloud.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String jwtSecret;
    private String frontendUrl = "http://localhost:5173";
    private String testRunnerDir = "../test-runner";
}
