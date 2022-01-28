package com.tui.proof.core.service;

import com.github.javafaker.Faker;
import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderRequest;
import com.tui.proof.core.domain.data.OrderSummary;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.core.domain.exception.BadPilotesOrderException;
import com.tui.proof.core.domain.exception.EditingClosedOrderException;
import com.tui.proof.core.domain.exception.ItemNotFoundException;
import com.tui.proof.core.domain.rules.OrderRules;
import com.tui.proof.core.gateway.OrderGateway;
import com.tui.proof.core.gateway.TimerGateway;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderUpdaterImplTest {

    @InjectMocks
    private OrderUpdaterImpl orderUpdater;

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private OrderRules orderRules;

    @Mock
    private TimerGateway timerGateway;

    @Captor
    ArgumentCaptor<Order> orderCaptor;

    @Test
    public void shouldUpdateOrderUntilTheEnd() {
        Faker faker = new Faker();
        Order orderAlreadyPresent = FakeOrder.buildOrder();
        String id = orderAlreadyPresent.getId();
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expectedOrder = FakeOrder.buildOrderWithId(id, orderAlreadyPresent.getOrderSummary().getCreatedAt(), orderAlreadyPresent.getOrderSummary().getEditableUntil(), request);
        when(timerGateway.now()).thenReturn(expectedOrder.getOrderSummary().getEditableUntil());
        when(orderRules.calculateTotal(request.getPilotes())).thenReturn(expectedOrder.getOrderSummary().getTotal());
        when(orderRules.allowedPilotes(request.getPilotes())).thenReturn(true);
        when(orderGateway.findById(any())).thenReturn(Optional.of(orderAlreadyPresent));
        when(orderGateway.update(any())).thenReturn(expectedOrder);
        Order savedOrder = orderUpdater.updateOrder(id, request);
        assertEquals(expectedOrder, savedOrder);
        //verify server side generated fields
        verify(orderGateway, times(1)).update(orderCaptor.capture());
        Order orderSendToGateway = orderCaptor.getValue();
        assertEquals(expectedOrder.getOrderSummary().getCreatedAt(), orderSendToGateway.getOrderSummary().getCreatedAt());
        assertEquals(expectedOrder.getOrderSummary().getEditableUntil(), orderSendToGateway.getOrderSummary().getEditableUntil());
        assertEquals(expectedOrder.getOrderSummary().getTotal(), orderSendToGateway.getOrderSummary().getTotal());
    }

    @Test
    public void shouldNotUpdateWithBadPilotes() {
        String id = "1";
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderRules.allowedPilotes(request.getPilotes())).thenReturn(false);
        assertThrows(BadPilotesOrderException.class, () -> {
            orderUpdater.updateOrder(id, request);
        });
    }

    @Test
    public void shouldNotUpdateAfterIsClosed() {
        Order orderAlreadyPresent = FakeOrder.buildOrder();
        String id = orderAlreadyPresent.getId();
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expectedOrder = FakeOrder.buildOrderWithId(id, orderAlreadyPresent.getOrderSummary().getCreatedAt(), orderAlreadyPresent.getOrderSummary().getEditableUntil(), request);
        Instant timeAfterClose = orderAlreadyPresent.getOrderSummary().getEditableUntil().plusNanos(1l);
        when(timerGateway.now()).thenReturn(timeAfterClose);
        when(orderRules.allowedPilotes(request.getPilotes())).thenReturn(true);
        when(orderGateway.findById(id)).thenReturn(Optional.of(orderAlreadyPresent));
        assertThrows(EditingClosedOrderException.class, () -> {
            orderUpdater.updateOrder(id, request);
        });
    }

    @Test
    public void shouldNotUpdateIfNotPresent() {
        String id = "1";
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderRules.allowedPilotes(request.getPilotes())).thenReturn(true);
        when(orderGateway.findById(id)).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> {
            orderUpdater.updateOrder(id, request);
        });
    }


}
