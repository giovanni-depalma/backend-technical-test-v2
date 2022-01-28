package com.tui.proof.core.service;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderRequest;
import com.tui.proof.core.domain.data.OrderSummary;
import com.tui.proof.core.domain.exception.BadPilotesOrderException;
import com.tui.proof.core.domain.rules.OrderRules;
import com.tui.proof.core.gateway.OrderGateway;
import com.tui.proof.core.gateway.TimerGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@Slf4j
@AllArgsConstructor
public class OrderCreatorImpl implements OrderCreator{
    private final OrderGateway orderGateway;
    private final OrderRules orderRules;
    private final TimerGateway timerGateway;

    @Override
    public Order createOrder(OrderRequest orderRequest) {
        log.debug("create order: {}", orderRequest);
        if(!orderRules.allowedPilotes(orderRequest.getPilotes()))
            throw new BadPilotesOrderException();
        Instant createdAt = timerGateway.now();
        Instant editableUntil = orderRules.calculateEditableUntil(createdAt);
        Order order = buildOrder(orderRequest, createdAt, editableUntil);
        return orderGateway.create(order);
    }

    private Order buildOrder(OrderRequest orderRequest, Instant createdAt, Instant editableUntil) {
        BigDecimal orderTotal = orderRules.calculateTotal(orderRequest.getPilotes());
        OrderSummary orderSummary = OrderSummary.builder().total(orderTotal)
                .pilotes(orderRequest.getPilotes()).createdAt(createdAt).editableUntil(editableUntil).build();
        return Order.builder().customer(orderRequest.getCustomer())
                .delivery(orderRequest.getDelivery())
                .orderSummary(orderSummary).build();
    }
}
