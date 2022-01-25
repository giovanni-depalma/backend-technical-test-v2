package com.tui.proof.core.usecases.order;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.core.domain.service.OrderService;
import com.tui.proof.core.gateway.OrderGateway;
import com.tui.proof.core.gateway.TimerGateway;
import com.tui.proof.core.usecases.UseCase;


import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
public class FindOrderByCustomerUseCase implements UseCase<FindOrderByCustomerUseCase.InputValues, FindOrderByCustomerUseCase.OutputValues> {
    private final OrderGateway orderGateway;
    private final OrderService orderService;

    @Override
    public OutputValues execute(InputValues input) {
        Stream<Order> found = orderGateway.findByCustomer(input.getCustomer());
        return new OutputValues(orderService.withStatus(found));
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        @NotNull
        private PersonalInfo customer;
    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {
        private final Stream<Order> orders;
    }

}
