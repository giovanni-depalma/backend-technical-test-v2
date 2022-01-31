package com.tui.proof.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "spring.main.security")
@Data
public class SecurityParameters {
    private String[] whiteList;
}
