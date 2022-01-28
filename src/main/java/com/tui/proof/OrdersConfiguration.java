package com.tui.proof;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.tui.proof.core.domain.rules.OrderRules;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;


@Configuration
@ConfigurationProperties(prefix = "spring.main.orders")
@Data
public class OrdersConfiguration {
    private Set<Integer> orderableQuantity;
    private BigDecimal price;
    private int closedAfterSeconds;

    @Bean
    public OrderRules createRules(){
        return new OrderRules(price, orderableQuantity, closedAfterSeconds);
    }
}
