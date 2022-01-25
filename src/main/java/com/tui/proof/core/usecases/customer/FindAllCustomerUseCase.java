package com.tui.proof.core.usecases.customer;

import java.time.Instant;
import java.util.List;

import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.service.OrderService;
import com.tui.proof.core.gateway.CustomerGateway;
import com.tui.proof.core.gateway.OrderGateway;
import com.tui.proof.core.gateway.TimerGateway;
import com.tui.proof.core.usecases.UseCase;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
public class FindAllCustomerUseCase
        implements UseCase<FindAllCustomerUseCase.InputValues, FindAllCustomerUseCase.OutputValues> {
    private final CustomerGateway customerGateway;

    @Override
    public OutputValues execute(InputValues input) {
        return new OutputValues(customerGateway.findAll()
                .toList());
    }

    @Value
    public static class InputValues implements UseCase.InputValues {

    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {
        private final List<Customer> customers;
    }

}
