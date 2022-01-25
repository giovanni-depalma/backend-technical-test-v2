package com.tui.proof.core.usecases.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import javax.validation.constraints.NotNull;
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
public class UpdateOrderUseCase implements UseCase<UpdateOrderUseCase.InputValues, UpdateOrderUseCase.OutputValues> {
    private final OrderGateway orderGateway;
    private final OrderService orderService;

    @Override
    public OutputValues execute(InputValues input) {
        Optional<Order> found = orderGateway.findById(input.getId());
        Optional<Order> updated = orderService.withStatus(found).filter(o -> Status.OPEN.equals(o.getStatus()))
                .map(savedOrder -> {
                    Order updatedOrder = orderGateway
                            .update(createOrder(input.getId(), savedOrder.getOrderSummary().getCreatedAt(), input));
                    return updatedOrder.withStatus(Status.OPEN);
                });
        return new OutputValues(updated);
    }

    private Order createOrder(String id, Instant createdAt, InputValues input) {
        Instant updatedAt = orderService.now();
        OrderRequest orderRequest = input.getOrderRequest();
        BigDecimal orderTotal = orderService.calculateTotal(orderRequest.getPilotes());
        OrderSummary orderSummary = OrderSummary.builder().id(id).total(orderTotal)
                .pilotes(orderRequest.getPilotes()).createdAt(createdAt).updatedAt(updatedAt).build();
        return Order.builder().status(Status.OPEN).customer(orderRequest.getCustomer())
                .delivery(orderRequest.getDelivery())
                .orderSummary(orderSummary)
                .build();
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        @NotNull
        private String id;
        @NotNull
        private OrderRequest orderRequest;
    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {
        private final Optional<Order> order;
    }

}
