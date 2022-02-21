package com.tui.proof.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.main.security")
@Data
public class WebSecurityConfigParameters {
    private String[] whiteList;
}
