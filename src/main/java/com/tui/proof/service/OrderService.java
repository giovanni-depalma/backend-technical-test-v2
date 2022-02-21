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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private OrderRules orderRules;
    private Clock clock;
    private CustomerService customerService;

    private Flux<Order> findByCustomerMap(Map<UUID, Customer> customers) {
        if (customers.isEmpty())
            return Flux.empty();
        return orderRepository.findByCustomerIn(customers.keySet()).map(o -> {
            Customer c = customers.get(o.getCustomer().getId());
            o.setCustomer(c);
            return o;
        });
    }

    public Flux<Order> findByCustomer(Customer customer) {
        log.debug("findByCustomer with by example {}", customer);
        return customerService.findByExample(customer)
                .collectMap(Customer::getId)
                .flatMapMany(this::findByCustomerMap)
                .onErrorMap(e -> {
                    log.error("error find by customer: {}", customer, e);
                    return errorMapper(e);
                });
    }


    @Transactional
    public Mono<Order> createOrder(OrderRequest orderRequest) {
        log.debug("create order: {}", orderRequest);
        return checkOrderRequest(orderRequest)
                .flatMap(check -> customerService.findByEmailAndSave(orderRequest.customer()))
                .flatMap(customer -> save(prepareNewOrder(), orderRequest, customer))
                .onErrorMap(e -> {
                    log.error("error create order: {}", orderRequest, e);
                    return errorMapper(e);
                });
    }

    private Order prepareNewOrder() {
        Instant createdAt = Instant.now(clock);
        Instant editableUntil = orderRules.calculateEditableUntil(createdAt);
        Order toSave = new Order();
        toSave.setCreatedAt(createdAt);
        toSave.setEditableUntil(editableUntil);
        return toSave;
    }

    @Transactional
    public Mono<Order> updateOrder(UUID id, OrderRequest orderRequest) {
        log.debug("update order with uuid {}, request: {}", id, orderRequest);
        return checkOrderRequest(orderRequest)
                .flatMap(o -> findByIdForUpdate(id))
                .zipWhen(o -> customerService.findByEmailAndSave(orderRequest.customer()))
                .flatMap(t -> save(t.getT1(), orderRequest, t.getT2()))
                .onErrorMap(e -> {
                    log.error("error update order with uuid {}, request: {}", id, orderRequest, e);
                    return errorMapper(e);
                });
    }

    private Throwable errorMapper(Throwable e) {
        return e instanceof ServiceException ? e : new ServiceException();
    }

    private Mono<Order> save(Order order, OrderRequest request, Customer customer) {
        log.debug("save request {}, order {} with customer {}", request, order, customer);
        Money total = orderRules.calculateTotal(request.pilotes());
        order.setCustomer(customer);
        order.setDelivery(request.delivery());
        order.setPilotes(request.pilotes());
        order.setTotal(total);
        return orderRepository.save(order).onErrorMap(e -> {
            log.error("save error, request {}, order {} with customer {}", request, order, customer, e);
            return errorMapper(e);
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
