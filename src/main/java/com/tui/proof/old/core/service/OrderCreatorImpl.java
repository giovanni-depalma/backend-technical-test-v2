package com.tui.proof.old.core.service;

import com.tui.proof.old.OrderOld;
import com.tui.proof.domain.entities.OrderRequest;
import com.tui.proof.domain.entities.OrderSummary;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.rules.OrderRules;
import com.tui.proof.old.core.gateway.OrderGateway;
import com.tui.proof.service.TimerService;
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
    private final TimerService timerGateway;

    @Override
    public OrderOld createOrder(OrderRequest orderRequest) {
        log.debug("create order: {}", orderRequest);
        if(!orderRules.allowedPilotes(orderRequest.getPilotes()))
            throw new BadPilotesOrderException();
        Instant createdAt = timerGateway.now();
        Instant editableUntil = orderRules.calculateEditableUntil(createdAt);
        OrderOld order = buildOrder(orderRequest, createdAt, editableUntil);
        return orderGateway.create(order);
    }

    private OrderOld buildOrder(OrderRequest orderRequest, Instant createdAt, Instant editableUntil) {
        BigDecimal orderTotal = orderRules.calculateTotal(orderRequest.getPilotes()).getValue();
        OrderSummary orderSummary = OrderSummary.builder().total(orderTotal)
                .pilotes(orderRequest.getPilotes()).createdAt(createdAt).editableUntil(editableUntil).build();
        return OrderOld.builder().customer(orderRequest.getCustomer())
                .delivery(orderRequest.getDelivery())
                .orderSummary(orderSummary).build();
    }
}
