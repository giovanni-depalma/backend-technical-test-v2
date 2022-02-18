package com.tui.proof.service;

import com.tui.proof.domain.entities.*;
import com.tui.proof.domain.entities.base.Money;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.domain.exception.ServiceException;
import com.tui.proof.domain.rules.OrderRules;
import com.tui.proof.mapper.CustomerMapper;
import com.tui.proof.presenter.api.CustomerResource;
import com.tui.proof.repository.OrderRepository;
import com.tui.proof.service.api.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private OrderRules orderRules;
    private Clock clock;
    private CustomerService customerService;

    public Flux<Order> findByCustomer(Customer customer) {
        try{
            log.debug("findByCustomer with by example {}", customer);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase();
            Order orderData = new Order();
            orderData.setCustomer(customer);
            return orderRepository.findAll(Example.of(orderData, matcher));
        }
        catch (Exception e){
            log.error("error finding {}", customer, e);
            return Flux.error(new ServiceException());
        }
    }

    public Mono<Order> createOrder(OrderRequest orderRequest) {
        try{
            log.debug("create order: {}", orderRequest);
            checkOrderRequest(orderRequest);
            return customerService.findByEmailAndSave(orderRequest.customer()).flatMap(customer -> {
                Instant createdAt = Instant.now(clock);
                Instant editableUntil = orderRules.calculateEditableUntil(createdAt);
                Money total = orderRules.calculateTotal(orderRequest.pilotes());
                Order toSave = new Order();
                toSave.setId(UUID.randomUUID());
                populateOrder(toSave, createdAt, editableUntil);
                toSave.setCustomer(customer);
                toSave.setDelivery(orderRequest.delivery());
                toSave.setPilotes(orderRequest.pilotes());
                toSave.setTotal(total);
                return orderRepository.save(toSave);
            });

        }
        catch (BadPilotesOrderException e){
            return Mono.error(e);
        }
        catch(Exception e){
            log.error("error saving: {}", orderRequest, e);
            return Mono.error(new ServiceException());
        }
    }

    public Mono<Order> updateOrder(UUID id, OrderRequest orderRequest) {
        try{
            log.debug("update order with uuid {}, request: {}", id, orderRequest);
            checkOrderRequest(orderRequest);
            return orderRepository.findById(id).switchIfEmpty(Mono.error(new ItemNotFoundException()))
                    .flatMap(savedOrder ->{
                        Instant now = Instant.now(clock);
                        if(now.isAfter(savedOrder.getEditableUntil())){
                            log.debug("editing closed order with id {}", id);
                            return Mono.error(new EditingClosedOrderException());
                        }
                        else{
                            log.debug("editing open order with id {}", id);
                            return customerService.findByEmailAndSave(orderRequest.customer()).flatMap(customer -> {
                                log.debug("editing open order with id {}, customer saved {}", id, customer);
                                Money total = orderRules.calculateTotal(orderRequest.pilotes());
                                populateOrder(savedOrder, savedOrder.getCreatedAt(), savedOrder.getEditableUntil());
                                savedOrder.setCustomer(customer);
                                savedOrder.setDelivery(orderRequest.delivery());
                                savedOrder.setPilotes(orderRequest.pilotes());
                                savedOrder.setTotal(total);
                                return orderRepository.save(savedOrder);
                            });
                        }
                    });
        }
        catch (BadPilotesOrderException e){
            return Mono.error(e);
        }
        catch(Exception e){
            log.error("error saving: " + orderRequest, e);
            return Mono.error(new EditingClosedOrderException());
        }
    }

    private void checkOrderRequest(OrderRequest orderRequest) throws BadPilotesOrderException {
        if(!orderRules.allowedPilotes(orderRequest.pilotes()))
            throw new BadPilotesOrderException();
    }

    private void populateOrder(Order order, Instant createdAt, Instant editableUntil){
        order.setCreatedAt(createdAt);
        order.setEditableUntil(editableUntil);
    }



}
