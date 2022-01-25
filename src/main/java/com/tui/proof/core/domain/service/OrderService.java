package com.tui.proof.core.domain.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.Status;
import com.tui.proof.core.gateway.TimerGateway;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderService {
    private final double priceForPilote;
    private final TimerGateway timerGateway;
  

    public BigDecimal calculateTotal(int pilotes) {
        BigDecimal price = new BigDecimal("1.33");
        return price.multiply(new BigDecimal(String.valueOf(pilotes)));
    }


    private Status getStatus(Instant now, Instant orderData) {
        return Duration.between(orderData, now).toSeconds() < 5 * 60 ? Status.OPEN : Status.CLOSED;
    }

    public Instant now(){
        return timerGateway.now();
    }
    public Stream<Order> withStatus(Stream<Order> stream) {
        Instant now = now();
        return stream.map(order -> order.withStatus(getStatus(now, order.getOrderSummary().getCreatedAt())));
    }

    public Optional<Order> withStatus(Optional<Order> optional) {
        Instant now = now();
        return optional.map(order -> order.withStatus(getStatus(now, order.getOrderSummary().getCreatedAt())));
    }

}
