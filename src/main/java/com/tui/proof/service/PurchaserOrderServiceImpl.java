package com.tui.proof.service;

import com.tui.proof.domain.entities.*;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.domain.rules.OrderRules;
import com.tui.proof.repositories.Order2DataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class PurchaserOrderServiceImpl implements PurchaserOrderService{
    private final Order2DataRepository orderRepository;
    private final OrderRules orderRules;
    private final TimerService timerGateway;
    private final CustomerService customerService;

    @Override
    public Order createOrder(OrderRequest orderRequest) {
        log.debug("create order: {}", orderRequest);
        if(!orderRules.allowedPilotes(orderRequest.getPilotes()))
            throw new BadPilotesOrderException();
        Customer customer2 = customerService.findByEmailAndSave(orderRequest.getCustomer());
        Instant createdAt = timerGateway.now();
        Instant editableUntil = orderRules.calculateEditableUntil(createdAt);
        Money total = orderRules.calculateTotal(orderRequest.getPilotes());
        Order toSave = new Order();
        toSave.setCreatedAt(createdAt);
        toSave.setEditableUntil(editableUntil);
        toSave.setCustomer(customer2);
        toSave.setDelivery(orderRequest.getDelivery());
        toSave.setPilotes(orderRequest.getPilotes());
        toSave.setTotal(total);
        return orderRepository.save(toSave);
    }

    @Override
    public Order updateOrder(UUID id, OrderRequest orderRequest) {
        log.debug("update order with uuid {}, request: {}", id, orderRequest);
        if(!orderRules.allowedPilotes(orderRequest.getPilotes()))
            throw new BadPilotesOrderException();
        Order savedOrder = orderRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        Instant now = timerGateway.now();
        if(now.isAfter(savedOrder.getEditableUntil())){
            log.debug("editing closed order with id {}", id);
            throw new EditingClosedOrderException();
        }
        else{
            log.debug("editing open order with id {}", id);
            Customer customer2 = customerService.findByEmailAndSave(orderRequest.getCustomer());
            Instant createdAt = savedOrder.getCreatedAt();
            Instant editableUntil = savedOrder.getEditableUntil();
            Money total = orderRules.calculateTotal(orderRequest.getPilotes());
            savedOrder.setCreatedAt(createdAt);
            savedOrder.setEditableUntil(editableUntil);
            savedOrder.setCustomer(customer2);
            savedOrder.setDelivery(orderRequest.getDelivery());
            savedOrder.setPilotes(orderRequest.getPilotes());
            savedOrder.setTotal(total);
            return orderRepository.save(savedOrder);
        }

    }

}
