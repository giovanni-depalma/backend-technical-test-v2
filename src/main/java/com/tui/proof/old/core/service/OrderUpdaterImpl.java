package com.tui.proof.old.core.service;

import com.tui.proof.old.OrderOld;
import com.tui.proof.domain.entities.OrderRequest;
import com.tui.proof.domain.entities.OrderSummary;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
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
public class OrderUpdaterImpl implements OrderUpdater{
    private final OrderGateway orderGateway;
    private final OrderRules orderRules;
    private final TimerService timerGateway;

    @Override
    public OrderOld updateOrder(String id, OrderRequest orderRequest) {
        log.debug("update order with id {}, request: {}", id, orderRequest);
        if(!orderRules.allowedPilotes(orderRequest.getPilotes()))
            throw new BadPilotesOrderException();
        OrderOld savedOrder = orderGateway.findById(id).orElseThrow(ItemNotFoundException::new);
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

    private OrderOld buildOrder(OrderRequest orderRequest, String id, Instant createdAt, Instant editableUntil) {
        BigDecimal orderTotal = orderRules.calculateTotal(orderRequest.getPilotes()).getValue();
        OrderSummary orderSummary = OrderSummary.builder().total(orderTotal)
                .pilotes(orderRequest.getPilotes()).createdAt(createdAt).editableUntil(editableUntil).build();
        return OrderOld.builder().id(id).customer(orderRequest.getCustomer())
                .delivery(orderRequest.getDelivery())
                .orderSummary(orderSummary).build();
    }
}
