package com.tui.proof.core.service;

import com.tui.proof.core.domain.data.*;
import com.tui.proof.core.domain.exception.BadPilotesOrderException;
import com.tui.proof.core.domain.rules.OrderRules;
import com.tui.proof.core.gateway.OrderGateway;
import com.tui.proof.core.gateway.TimerGateway;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderCreatorImplTest {
    @Mock
    private OrderGateway orderGateway;

    @Mock
    private OrderRules orderRules;

    @Mock
    private TimerGateway timerGateway;

    @Captor
    ArgumentCaptor<Order> orderCaptor;

    @InjectMocks
    private OrderCreatorImpl orderCreator;

    @Test
    public void shouldCreateOrder() {
        OrderRequest request = FakeOrder.buildOrderRequest();
        Order expectedOrder = FakeOrder.buildOrder(request);
        Instant now = expectedOrder.getOrderSummary().getCreatedAt();
        when(timerGateway.now()).thenReturn(now);
        when(orderRules.calculateTotal(request.getPilotes())).thenReturn(expectedOrder.getOrderSummary().getTotal());
        when(orderRules.allowedPilotes(request.getPilotes())).thenReturn(true);
        when(orderRules.calculateEditableUntil(now)).thenReturn(expectedOrder.getOrderSummary().getEditableUntil());
        when(orderGateway.create(any())).thenReturn(expectedOrder);
        Order savedOrder = orderCreator.createOrder(request);
        assertEquals(expectedOrder, savedOrder);
        //verify server side generated fields
        verify(orderGateway, times(1)).create(orderCaptor.capture());
        Order orderSendedToGateway = orderCaptor.getValue();
        assertEquals(expectedOrder.getOrderSummary().getCreatedAt(), orderSendedToGateway.getOrderSummary().getCreatedAt() );
        assertEquals(expectedOrder.getOrderSummary().getEditableUntil() , orderSendedToGateway.getOrderSummary().getEditableUntil() );
        assertEquals(expectedOrder.getOrderSummary().getTotal(), orderSendedToGateway.getOrderSummary().getTotal() );
    }

    @Test
    public void shouldNotCreateOrder() {
        OrderRequest request = FakeOrder.buildOrderRequest();
        when(orderRules.allowedPilotes(request.getPilotes())).thenReturn(false);
        assertThrows(BadPilotesOrderException.class, () -> orderCreator.createOrder(request));
    }
}
