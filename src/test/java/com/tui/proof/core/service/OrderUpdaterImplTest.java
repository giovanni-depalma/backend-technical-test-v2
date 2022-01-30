package com.tui.proof.core.service;

import com.tui.proof.domain.entities.Money;
import com.tui.proof.old.OrderOld;
import com.tui.proof.domain.entities.OrderRequest;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.domain.rules.OrderRules;
import com.tui.proof.old.core.gateway.OrderGateway;
import com.tui.proof.service.TimerService;
import com.tui.proof.old.core.service.OrderUpdaterImpl;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private TimerService timerGateway;

    @Captor
    ArgumentCaptor<OrderOld> orderCaptor;

    @Test
    public void shouldUpdateOrderUntilTheEnd() {
        OrderOld orderAlreadyPresent = FakeOrder.buildOrder();
        String id = orderAlreadyPresent.getId();
        OrderRequest request = FakeOrder.buildOrderRequest();
        OrderOld expectedOrder = FakeOrder.buildOrderWithId(id, orderAlreadyPresent.getOrderSummary().getCreatedAt(), orderAlreadyPresent.getOrderSummary().getEditableUntil(), request);
        when(timerGateway.now()).thenReturn(expectedOrder.getOrderSummary().getEditableUntil());
        when(orderRules.calculateTotal(request.getPilotes())).thenReturn(new Money(expectedOrder.getOrderSummary().getTotal()));
        when(orderRules.allowedPilotes(request.getPilotes())).thenReturn(true);
        when(orderGateway.findById(any())).thenReturn(Optional.of(orderAlreadyPresent));
        when(orderGateway.update(any())).thenReturn(expectedOrder);
        OrderOld savedOrder = orderUpdater.updateOrder(id, request);
        assertEquals(expectedOrder, savedOrder);
        //verify server side generated fields
        verify(orderGateway, times(1)).update(orderCaptor.capture());
        OrderOld orderSendToGateway = orderCaptor.getValue();
        assertEquals(expectedOrder.getOrderSummary().getCreatedAt(), orderSendToGateway.getOrderSummary().getCreatedAt());
        assertEquals(expectedOrder.getOrderSummary().getEditableUntil(), orderSendToGateway.getOrderSummary().getEditableUntil());
        assertEquals(expectedOrder.getOrderSummary().getTotal(), orderSendToGateway.getOrderSummary().getTotal());
    }

    @Test
    public void shouldNotUpdateWithBadPilotes() {
        String id = "1";
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderRules.allowedPilotes(request.getPilotes())).thenReturn(false);
        assertThrows(BadPilotesOrderException.class, () -> orderUpdater.updateOrder(id, request));
    }

    @Test
    public void shouldNotUpdateAfterIsClosed() {
        OrderOld orderAlreadyPresent = FakeOrder.buildOrder();
        String id = orderAlreadyPresent.getId();
        OrderRequest request = FakeOrder.buildOrderRequest();
        Instant timeAfterClose = orderAlreadyPresent.getOrderSummary().getEditableUntil().plusNanos(1L);
        when(timerGateway.now()).thenReturn(timeAfterClose);
        when(orderRules.allowedPilotes(request.getPilotes())).thenReturn(true);
        when(orderGateway.findById(id)).thenReturn(Optional.of(orderAlreadyPresent));
        assertThrows(EditingClosedOrderException.class, () -> orderUpdater.updateOrder(id, request));
    }

    @Test
    public void shouldNotUpdateIfNotPresent() {
        String id = "1";
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderRules.allowedPilotes(request.getPilotes())).thenReturn(true);
        when(orderGateway.findById(id)).thenReturn(Optional.empty());
        assertThrows(ItemNotFoundException.class, () -> orderUpdater.updateOrder(id, request));
    }


}
