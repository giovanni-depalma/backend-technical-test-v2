package com.tui.proof.old;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;


@Configuration
@ConfigurationProperties(prefix = "spring.main.orders")
@Data
public class OrdersConfiguration {
    private List<Integer> orderableQuantity;
    private double price;
}
