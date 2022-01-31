package com.tui.proof.service;

import com.tui.proof.domain.entities.*;
import com.tui.proof.domain.entities.base.Money;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.domain.exception.ServiceException;
import com.tui.proof.domain.rules.OrderRules;
import com.tui.proof.repositories.OrderRepository;
import com.tui.proof.service.data.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class PurchaserOrderServiceImpl implements PurchaserOrderService{
    private final OrderRepository orderRepository;
    private final OrderRules orderRules;
    private final TimerService timerGateway;
    private final CustomerService customerService;



    @Override
    public Order createOrder(OrderRequest orderRequest) throws BadPilotesOrderException {
        try{
            log.debug("create order: {}", orderRequest);
            checkOrderRequest(orderRequest);
            Customer customer = customerService.findByEmailAndSave(orderRequest.getCustomer());
            Instant createdAt = timerGateway.now();
            Instant editableUntil = orderRules.calculateEditableUntil(createdAt);
            Money total = orderRules.calculateTotal(orderRequest.getPilotes());
            Order toSave = new Order();
            populateOrder(toSave, createdAt, editableUntil);
            toSave.setCustomer(customer);
            toSave.setDelivery(orderRequest.getDelivery());
            toSave.setPilotes(orderRequest.getPilotes());
            toSave.setTotal(total);
            return orderRepository.save(toSave);
        }
        catch (BadPilotesOrderException e){
            throw e;
        }
        catch(Exception e){
            log.error("error saving: {}", orderRequest, e);
            throw new ServiceException();
        }
    }

    @Override
    public Order updateOrder(UUID id, OrderRequest orderRequest) throws EditingClosedOrderException, ItemNotFoundException, BadPilotesOrderException {
        try{
            log.debug("update order with uuid {}, request: {}", id, orderRequest);
            checkOrderRequest(orderRequest);
            Order savedOrder = orderRepository.findById(id).orElseThrow(ItemNotFoundException::new);
            Instant now = timerGateway.now();
            if(now.isAfter(savedOrder.getEditableUntil())){
                log.debug("editing closed order with id {}", id);
                throw new EditingClosedOrderException();
            }
            else{
                log.debug("editing open order with id {}", id);
                Customer customer2 = customerService.findByEmailAndSave(orderRequest.getCustomer());
                Money total = orderRules.calculateTotal(orderRequest.getPilotes());
                populateOrder(savedOrder, savedOrder.getCreatedAt(), savedOrder.getEditableUntil());
                savedOrder.setCustomer(customer2);
                savedOrder.setDelivery(orderRequest.getDelivery());
                savedOrder.setPilotes(orderRequest.getPilotes());
                savedOrder.setTotal(total);
                return orderRepository.save(savedOrder);
            }
        }
        catch (BadPilotesOrderException | EditingClosedOrderException | ItemNotFoundException e){
            throw e;
        }
        catch(Exception e){
            log.error("error saving: " + orderRequest, e);
            throw new ServiceException();
        }
    }

    private void checkOrderRequest(OrderRequest orderRequest) throws BadPilotesOrderException {
        if(!orderRules.allowedPilotes(orderRequest.getPilotes()))
            throw new BadPilotesOrderException();
    }

    private void populateOrder(Order order, Instant createdAt, Instant editableUntil){
        order.setCreatedAt(createdAt);
        order.setEditableUntil(editableUntil);
    }

}
