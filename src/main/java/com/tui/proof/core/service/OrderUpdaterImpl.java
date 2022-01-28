package com.tui.proof.core.service;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderRequest;
import com.tui.proof.core.domain.data.OrderSummary;
import com.tui.proof.core.domain.exception.BadPilotesOrderException;
import com.tui.proof.core.domain.exception.EditingClosedOrderException;
import com.tui.proof.core.domain.exception.ItemNotFoundException;
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
public class OrderUpdaterImpl implements OrderUpdater{
    private final OrderGateway orderGateway;
    private final OrderRules orderRules;
    private final TimerGateway timerGateway;

    @Override
    public Order updateOrder(String id, OrderRequest orderRequest) {
        log.debug("update order with id {}, request: {}", id, orderRequest);
        if(!orderRules.allowedPilotes(orderRequest.getPilotes()))
            throw new BadPilotesOrderException();
        Order savedOrder = orderGateway.findById(id).orElseThrow(ItemNotFoundException::new);
        Instant now = timerGateway.now();
        if(now.isAfter(savedOrder.getOrderSummary().getEditableUntil())){
            log.debug("editing closed order with id {}", id);
            throw new EditingClosedOrderException();
        }
        else{
            log.debug("editing open order with id {}", id);
            Instant createdAt = savedOrder.getOrderSummary().getCreatedAt();
            Instant editableUntil = savedOrder.getOrderSummary().getEditableUntil();
            return orderGateway
                    .update(buildOrder(orderRequest, id, createdAt, editableUntil));
        }
    }

    private Order buildOrder(OrderRequest orderRequest, String id, Instant createdAt, Instant editableUntil) {
        BigDecimal orderTotal = orderRules.calculateTotal(orderRequest.getPilotes());
        OrderSummary orderSummary = OrderSummary.builder().total(orderTotal)
                .pilotes(orderRequest.getPilotes()).createdAt(createdAt).editableUntil(editableUntil).build();
        return Order.builder().id(id).customer(orderRequest.getCustomer())
                .delivery(orderRequest.getDelivery())
                .orderSummary(orderSummary).build();
    }
}
