package com.tui.proof.config;

import java.math.BigDecimal;
import java.util.Set;

import com.tui.proof.domain.entities.base.Money;
import com.tui.proof.domain.rules.OrderRules;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;


@Configuration
@ConfigurationProperties(prefix = "spring.main.orders")
@Data
public class    OrderConfig {
    private Set<Integer> allowedQuantity;
    private BigDecimal price;
    private int closedAfterSeconds;

    @Bean
    public OrderRules createRules() {
        return new OrderRules(new Money(price), allowedQuantity, closedAfterSeconds);
    }
}
