package com.tui.proof.core.service;

import com.tui.proof.domain.entities.Money;
import com.tui.proof.old.OrderOld;
import com.tui.proof.domain.entities.OrderRequest;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.rules.OrderRules;
import com.tui.proof.old.core.gateway.OrderGateway;
import com.tui.proof.service.TimerService;
import com.tui.proof.old.core.service.OrderCreatorImpl;
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
    private TimerService timerGateway;

    @Captor
    ArgumentCaptor<OrderOld> orderCaptor;

    @InjectMocks
    private OrderCreatorImpl orderCreator;

    @Test
    public void shouldCreateOrder() {
        OrderRequest request = FakeOrder.buildOrderRequest();
        OrderOld expectedOrder = FakeOrder.buildOrder(request);
        Instant now = expectedOrder.getOrderSummary().getCreatedAt();
        when(timerGateway.now()).thenReturn(now);
        when(orderRules.calculateTotal(request.getPilotes())).thenReturn(new Money(expectedOrder.getOrderSummary().getTotal()));
        when(orderRules.allowedPilotes(request.getPilotes())).thenReturn(true);
        when(orderRules.calculateEditableUntil(now)).thenReturn(expectedOrder.getOrderSummary().getEditableUntil());
        when(orderGateway.create(any())).thenReturn(expectedOrder);
        OrderOld savedOrder = orderCreator.createOrder(request);
        assertEquals(expectedOrder, savedOrder);
        //verify server side generated fields
        verify(orderGateway, times(1)).create(orderCaptor.capture());
        OrderOld orderSendedToGateway = orderCaptor.getValue();
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
