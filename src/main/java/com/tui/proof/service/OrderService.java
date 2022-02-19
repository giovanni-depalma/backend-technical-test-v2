package com.tui.proof.service;

import com.tui.proof.domain.entities.*;
import com.tui.proof.domain.entities.base.Money;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
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
        try {
            log.debug("findByCustomer with by example {}", customer);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase();
            Order orderData = new Order();
            orderData.setCustomer(customer);
            return orderRepository.findAll(Example.of(orderData, matcher))
                    .onErrorMap(e -> findByCustomerError(e,customer));
        } catch (Exception e) {
            return Flux.error(findByCustomerError(e,customer));
        }
    }

    private ServiceException findByCustomerError(Throwable e, Customer customer){
        log.error("error finding {}", customer, e);
        return new ServiceException();
    }

    public Mono<Order> createOrder(OrderRequest orderRequest) {
        log.debug("create order: {}", orderRequest);
        return checkOrderRequest(orderRequest)
                .flatMap(check -> customerService.findByEmailAndSave(orderRequest.customer()))
                .flatMap(customer -> save(prepareNewOrder(), orderRequest, customer));
    }

    private Order prepareNewOrder(){
        Instant createdAt = Instant.now(clock);
        Instant editableUntil = orderRules.calculateEditableUntil(createdAt);
        Order toSave = new Order();
        toSave.setId(UUID.randomUUID());
        toSave.setCreatedAt(createdAt);
        toSave.setEditableUntil(editableUntil);
        return toSave;
    }

    public Mono<Order> updateOrder(UUID id, OrderRequest orderRequest) {
        try {
            log.debug("update order with uuid {}, request: {}", id, orderRequest);
            return checkOrderRequest(orderRequest)
                    .then(findByIdForUpdate(id))
                    .zipWhen( o -> customerService.findByEmailAndSave(orderRequest.customer()))
                    .flatMap(t -> save(t.getT1(),orderRequest,t.getT2()));
        } catch (Exception e) {
            log.error("error saving {}", orderRequest, e);
            return Mono.error(new ServiceException());
        }
    }

    private Mono<Order> save(Order order, OrderRequest request, Customer customer){
        log.debug("save request {}, order {} with customer {}", request, order, customer);
        Money total = orderRules.calculateTotal(request.pilotes());
        order.setCustomer(customer);
        order.setDelivery(request.delivery());
        order.setPilotes(request.pilotes());
        order.setTotal(total);
        return orderRepository.save(order).onErrorMap(e ->{
            log.error("save error, request {}, order {} with customer {}", request, order, customer,e);
            throw new ServiceException();
        });
    }

    private Mono<Boolean> checkOrderRequest(OrderRequest orderRequest) {
        if (!orderRules.allowedPilotes(orderRequest.pilotes()))
            return Mono.error(new BadPilotesOrderException());
        else
            return Mono.just(true);
    }

    private Mono<Order> findByIdForUpdate(UUID id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new ItemNotFoundException()))
                .handle((item, sink) -> {
                    Instant now = Instant.now(clock);
                    if (now.isAfter(item.getEditableUntil())) {
                        log.debug("editing closed order {}", id);
                        sink.error(new EditingClosedOrderException());
                    } else {
                        sink.next(item);
                    }
                });
    }


}
