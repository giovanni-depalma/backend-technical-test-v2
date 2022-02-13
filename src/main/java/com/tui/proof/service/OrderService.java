package com.tui.proof.service;

import com.tui.proof.domain.entities.*;
import com.tui.proof.domain.entities.base.Money;
import com.tui.proof.domain.entities.base.PersonalInfo;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.domain.exception.ServiceException;
import com.tui.proof.domain.rules.OrderRules;
import com.tui.proof.repository.OrderRepository;
import com.tui.proof.service.api.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderRules orderRules;
    private final Clock clock;
    private final CustomerService customerService;

    public List<Order> findByCustomer(PersonalInfo personalInfo) {
        try{
            log.debug("findByCustomer with by example {}", personalInfo);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase();
            Order orderData = new Order();
            Customer customer = new Customer();
            customer.setPersonalInfo(personalInfo);
            orderData.setCustomer(customer);
            return orderRepository.findAll(Example.of(orderData, matcher));
        }
        catch (Exception e){
            log.error("error finding {}", personalInfo, e);
            throw new ServiceException();
        }
    }

    public Order createOrder(OrderRequest orderRequest) throws BadPilotesOrderException {
        try{
            log.debug("create order: {}", orderRequest);
            checkOrderRequest(orderRequest);
            Customer customer = customerService.findByEmailAndSave(orderRequest.getCustomer());
            Instant createdAt = Instant.now(clock);
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

    public Order updateOrder(UUID id, OrderRequest orderRequest) throws EditingClosedOrderException, ItemNotFoundException, BadPilotesOrderException {
        try{
            log.debug("update order with uuid {}, request: {}", id, orderRequest);
            checkOrderRequest(orderRequest);
            Order savedOrder = orderRepository.findById(id).orElseThrow(ItemNotFoundException::new);
            Instant now = Instant.now(clock);
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
