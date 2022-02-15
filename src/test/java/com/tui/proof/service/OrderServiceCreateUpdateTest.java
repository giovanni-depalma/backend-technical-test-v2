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

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void shouldCreateOrder() throws BadPilotesOrderException {
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expectedOrder = FakeOrder.buildOrder(request);
        Instant now = expectedOrder.getCreatedAt();
        when(clock.instant()).thenReturn(now);
        when(orderRules.calculateTotal(request.pilotes())).thenReturn(expectedOrder.getTotal());
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(true);
        when(orderRules.calculateEditableUntil(now)).thenReturn(expectedOrder.getEditableUntil());
        when(orderRepository.save(any())).thenReturn(expectedOrder);
        Order savedOrder = service.createOrder(request);
        assertEquals(expectedOrder, savedOrder);
        assertEquals(expectedOrder.getCustomer(), savedOrder.getCustomer());
        verify(customerService, times(1)).save(expectedOrder.getCustomer());
        //verify server side generated fields
        verify(orderRepository, times(1)).save(orderCaptor.capture());
        Order orderSentToRepository = orderCaptor.getValue();
        assertEquals(expectedOrder.getCreatedAt(), orderSentToRepository.getCreatedAt() );
        assertEquals(expectedOrder.getEditableUntil() , orderSentToRepository.getEditableUntil() );
        assertEquals(expectedOrder.getTotal(), orderSentToRepository.getTotal() );
    }

    @Test
    public void shouldNotCreateAfterInternalError() {
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderRules.allowedPilotes(request.pilotes())).thenThrow(RuntimeException.class);
        assertThrows(ServiceException.class, () -> service.createOrder(request));
    }

    @Test
    public void shouldNotCreateOrder() {
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(false);
        assertThrows(BadPilotesOrderException.class, () -> service.createOrder(request));
    }

    @Test
    public void shouldUpdateOrderUntilTheEnd() throws BadPilotesOrderException, EditingClosedOrderException, ItemNotFoundException {
        Order orderAlreadyPresent = FakeOrder.buildOrder();
        UUID id = orderAlreadyPresent.getId();
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expectedOrder = FakeOrder.buildOrderWithId(id, orderAlreadyPresent.getCreatedAt(), orderAlreadyPresent.getEditableUntil(), request);
        when(clock.instant()).thenReturn(expectedOrder.getEditableUntil());
        when(orderRepository.save(any())).thenReturn(expectedOrder);
        when(orderRules.calculateTotal(request.pilotes())).thenReturn(expectedOrder.getTotal());
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(true);
        when(orderRepository.findById(any())).thenReturn(Optional.of(orderAlreadyPresent));
        when(orderRepository.save(any())).thenReturn(expectedOrder);
        Order savedOrder = service.updateOrder(id, request);
        assertEquals(expectedOrder, savedOrder);
        //verify server side generated fields
        verify(orderRepository, times(1)).save(orderCaptor.capture());
        Order orderSentToGateway = orderCaptor.getValue();
        assertEquals(expectedOrder.getCreatedAt(), orderSentToGateway.getCreatedAt());
        assertEquals(expectedOrder.getEditableUntil(), orderSentToGateway.getEditableUntil());
        assertEquals(expectedOrder.getTotal(), orderSentToGateway.getTotal());
    }

    @Test
    public void shouldNotUpdateWithBadPilotes() {
        UUID id = UUID.randomUUID();
        OrderRequest request = FakeOrder.buildBadOrderRequest();
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(false);
        assertThrows(BadPilotesOrderException.class, () -> service.updateOrder(id, request));
    }

    @Test
    public void shouldNotUpdateAfterIsClosed() {
        Order orderAlreadyPresent = FakeOrder.buildOrder();
        UUID id = orderAlreadyPresent.getId();
        OrderRequest request = FakeOrder.buildOrderRequest();
        Instant timeAfterClose = orderAlreadyPresent.getEditableUntil().plusNanos(1L);
        when(clock.instant()).thenReturn(timeAfterClose);
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(true);
        when(orderRepository.findById(id)).thenReturn(Optional.of(orderAlreadyPresent));
        assertThrows(EditingClosedOrderException.class, () -> service.updateOrder(id, request));
    }

    @Test
    public void shouldNotUpdateIfNotPresent() {
        UUID id = UUID.randomUUID();
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(true);
        when(orderRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> service.updateOrder(id, request));
    }

    @Test
    public void shouldNotUpdateAfterInternalError() {
        Order orderAlreadyPresent = FakeOrder.buildOrder();
        UUID id = orderAlreadyPresent.getId();
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderRules.allowedPilotes(request.pilotes())).thenReturn(true);
        when(orderRepository.findById(id)).thenThrow(RuntimeException.class);
        assertThrows(ServiceException.class, () -> service.updateOrder(id, request));
    }
}
