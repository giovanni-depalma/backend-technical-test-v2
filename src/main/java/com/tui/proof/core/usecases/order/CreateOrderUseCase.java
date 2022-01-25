package com.tui.proof.core.usecases.order;

import java.math.BigDecimal;
import java.time.Instant;
import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderRequest;
import com.tui.proof.core.domain.data.OrderSummary;
import com.tui.proof.core.domain.data.Status;
import com.tui.proof.core.domain.service.OrderService;
import com.tui.proof.core.gateway.OrderGateway;
import com.tui.proof.core.usecases.UseCase;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
public class CreateOrderUseCase implements UseCase<CreateOrderUseCase.InputValues, CreateOrderUseCase.OutputValues>{
    private final OrderGateway orderGateway;
    private final OrderService orderService;

    @Override
    public OutputValues execute(InputValues input) {
        Order order = createOrder(input);
        Order saved = orderGateway.create(order);
        return new OutputValues(saved.withStatus(Status.OPEN));
    }

    private Order createOrder(InputValues input) {
        Instant now = orderService.now();
        OrderRequest orderRequest = input.getOrderRequest();
        BigDecimal orderTotal = orderService.calculateTotal(orderRequest.getPilotes());
        OrderSummary orderSummary = OrderSummary.builder().total(orderTotal)
                .pilotes(orderRequest.getPilotes()).createdAt(now).updatedAt(now).build();
        return Order.builder().status(Status.OPEN).customer(orderRequest.getCustomer()).delivery(orderRequest.getDelivery())
                .orderSummary(orderSummary)
                .build();
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        private final OrderRequest orderRequest;
    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {
        private final Order order;
    }

}
