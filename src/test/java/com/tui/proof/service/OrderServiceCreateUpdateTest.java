package com.tui.proof.service;

import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.exception.ServiceException;
import com.tui.proof.service.api.OrderRequest;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.domain.rules.OrderRules;
import com.tui.proof.repository.OrderRepository;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceCreateUpdateTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderRules orderRules;

    @Mock
    private Clock clock;

    @Mock
    private CustomerService customerService;

    @Captor
    ArgumentCaptor<Order> orderCaptor;

    @InjectMocks
    private OrderService service;

    @Test
    public void shouldCreateOrder() {
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expectedOrder = FakeOrder.buildOrder(request);
        Instant now = expectedOrder.getCreatedAt();
        when(clock.instant()).thenReturn(now);
        when(orderRules.calculateTotal(request.pilotes())).thenReturn(expectedOrder.getTotal());
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(true);
        when(customerService.findByEmailAndSave(any())).thenReturn(Mono.just(expectedOrder.getCustomer()));
        when(orderRules.calculateEditableUntil(now)).thenReturn(expectedOrder.getEditableUntil());
        when(orderRepository.save(any())).thenReturn(Mono.just(expectedOrder));
        Mono<Order> actual = service.createOrder(request);
        StepVerifier.create(actual).consumeNextWith(savedOrder -> {
            assertEquals(expectedOrder, savedOrder);
            assertEquals(expectedOrder.getCustomer(), savedOrder.getCustomer());
            verify(customerService, times(1)).findByEmailAndSave(expectedOrder.getCustomer());
            //verify server side generated fields
            verify(orderRepository, times(1)).save(orderCaptor.capture());
            Order orderSentToRepository = orderCaptor.getValue();
            assertEquals(expectedOrder.getCreatedAt(), orderSentToRepository.getCreatedAt() );
            assertEquals(expectedOrder.getEditableUntil() , orderSentToRepository.getEditableUntil() );
            assertEquals(expectedOrder.getTotal(), orderSentToRepository.getTotal() );
        }).verifyComplete();

    }

    @Test
    public void shouldNotCreateAfterRepositoryError() {
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expectedOrder = FakeOrder.buildOrder(request);
        Instant now = expectedOrder.getCreatedAt();
        when(clock.instant()).thenReturn(now);
        when(orderRules.calculateTotal(request.pilotes())).thenReturn(expectedOrder.getTotal());
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(true);
        when(customerService.findByEmailAndSave(any())).thenReturn(Mono.just(expectedOrder.getCustomer()));
        when(orderRules.calculateEditableUntil(now)).thenReturn(expectedOrder.getEditableUntil());
        when(orderRepository.save(any())).thenReturn(Mono.error(new RuntimeException()));
        Mono<Order> actual = service.createOrder(request);
        StepVerifier.create(actual).expectError(ServiceException.class).verify();
    }

    @Test
    public void shouldNotCreateOrder() {
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(false);
        Mono<Order> actual = service.createOrder(request);
        StepVerifier.create(actual).expectError(BadPilotesOrderException.class).verify();
    }


    @Test
    public void shouldUpdateOrderUntilTheEnd() {
        Order orderAlreadyPresent = FakeOrder.buildOrder();
        UUID id = orderAlreadyPresent.getId();
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expectedOrder = FakeOrder.buildOrderWithId(id, orderAlreadyPresent.getCreatedAt(), orderAlreadyPresent.getEditableUntil(), request);
        when(clock.instant()).thenReturn(expectedOrder.getEditableUntil());
        when(orderRepository.save(any())).thenReturn(Mono.just(expectedOrder));
        when(orderRules.calculateTotal(request.pilotes())).thenReturn(expectedOrder.getTotal());
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(true);
        when(customerService.findByEmailAndSave(any())).thenReturn(Mono.just(expectedOrder.getCustomer()));
        when(orderRepository.findById(id)).thenReturn(Mono.just(orderAlreadyPresent));
        when(orderRepository.save(any())).thenReturn(Mono.just(expectedOrder));
        Mono<Order> actual = service.updateOrder(id, request);
        StepVerifier.create(actual).consumeNextWith(savedOrder -> {
            assertEquals(expectedOrder, savedOrder);
            //verify server side generated fields
            verify(orderRepository, times(1)).save(orderCaptor.capture());
            Order orderSentToGateway = orderCaptor.getValue();
            assertEquals(expectedOrder.getCreatedAt(), orderSentToGateway.getCreatedAt());
            assertEquals(expectedOrder.getEditableUntil(), orderSentToGateway.getEditableUntil());
            assertEquals(expectedOrder.getTotal(), orderSentToGateway.getTotal());
        }).verifyComplete();

    }

    @Test
    public void shouldNotCreateWithBadPilotes() {
        OrderRequest request = FakeOrder.buildBadOrderRequest();
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(false);
        Mono<Order> actual = service.createOrder(request);
        StepVerifier.create(actual).expectError(BadPilotesOrderException.class).verify();
    }

    @Test
    public void shouldNotUpdateWithBadPilotes() {
        Order orderAlreadyPresent = FakeOrder.buildOrder();
        UUID id = orderAlreadyPresent.getId();
        OrderRequest request = FakeOrder.buildBadOrderRequest();
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(false);
        Mono<Order> actual = service.updateOrder(id, request);
        StepVerifier.create(actual).expectError(BadPilotesOrderException.class).verify();
    }

    @Test
    public void shouldNotUpdateAfterIsClosed() {
        Order orderAlreadyPresent = FakeOrder.buildOrder();
        UUID id = orderAlreadyPresent.getId();
        OrderRequest request = FakeOrder.buildOrderRequest();
        Instant timeAfterClose = orderAlreadyPresent.getEditableUntil().plusNanos(1L);
        when(clock.instant()).thenReturn(timeAfterClose);
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(true);
        when(orderRepository.findById(id)).thenReturn(Mono.just(orderAlreadyPresent));
        //when(customerService.findByEmailAndSave(any())).thenReturn(Mono.just(orderAlreadyPresent.getCustomer()));
        Mono<Order> actual = service.updateOrder(id, request);
        StepVerifier.create(actual).expectError(EditingClosedOrderException.class).verify();
    }

    @Test
    public void shouldNotUpdateIfNotPresent() {
        UUID id = UUID.randomUUID();
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(true);
        when(orderRepository.findById(id)).thenReturn(Mono.empty());
        Mono<Order> actual = service.updateOrder(id, request);
        StepVerifier.create(actual).expectError(ItemNotFoundException.class).verify();
    }

    @Test
    public void shouldNotUpdateAfterInternalError() {
        Order orderAlreadyPresent = FakeOrder.buildOrder();
        UUID id = orderAlreadyPresent.getId();
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(true);
        when(orderRepository.findById(id)).thenReturn(Mono.error(new RuntimeException("error")));
        Mono<Order> actual = service.updateOrder(id, request);
        StepVerifier.create(actual).expectError(ServiceException.class).verify();
    }
}
