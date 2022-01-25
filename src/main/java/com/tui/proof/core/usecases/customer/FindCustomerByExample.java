package com.tui.proof.core.usecases.customer;

import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.core.gateway.CustomerGateway;
import com.tui.proof.core.usecases.UseCase;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
public class FindCustomerByExample
        implements UseCase<FindCustomerByExample.InputValues, FindCustomerByExample.OutputValues> {
    private final CustomerGateway customerGateway;

    @Override
    public OutputValues execute(InputValues input) {
        return new OutputValues(customerGateway.findByExample(input.getCustomer()));
    }

    @Value
    public static class InputValues implements UseCase.InputValues {
        @NotNull
        private PersonalInfo customer;
    }

    @Value
    public static class OutputValues implements UseCase.OutputValues {
        private final Stream<Customer> customers;
    }

}
