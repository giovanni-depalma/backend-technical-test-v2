package com.tui.proof.core.usecases.order;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.service.OrderService;
import com.tui.proof.core.gateway.OrderGateway;
import com.tui.proof.core.gateway.TimerGateway;
import com.tui.proof.core.usecases.UseCase;


import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
public class FindByIdOrderUseCase implements UseCase<FindByIdOrderUseCase.InputValues, FindByIdOrderUseCase.OutputValues> {
    private final OrderGateway orderGateway;
    private final OrderService orderService;

    @Override
    public OutputValues execute(InputValues input) {
        Optional<Order> found = orderGateway.findById(input.getId());
        return new OutputValues(orderService.withStatus(found));
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        private String id;
    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {
        private final Optional<Order> order;
    }

}
