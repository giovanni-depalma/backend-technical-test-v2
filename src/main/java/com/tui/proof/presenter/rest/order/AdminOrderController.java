package com.tui.proof.presenter.rest.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;

import com.tui.proof.core.domain.data.Address;
import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderSummary;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.core.usecases.UseCaseExecutor;
import com.tui.proof.core.usecases.order.FindAllOrderUseCase;
import com.tui.proof.core.usecases.order.FindByIdOrderUseCase;
import com.tui.proof.core.usecases.order.FindOrderByCustomerUseCase;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Value;

@RestController
@RequestMapping("/admin/orders")
@AllArgsConstructor
public class AdminOrderController {

    private final UseCaseExecutor executor;
    private final FindAllOrderUseCase findAllOrderUseCase;
    private final FindByIdOrderUseCase findByIdOrderUseCase;
    private final FindOrderByCustomerUseCase findOrderByCustomerUseCase;

    @GetMapping
    public Stream<Order> findAll() {
        return executor.execute(findAllOrderUseCase, new FindAllOrderUseCase.InputValues(),
                output -> output.getOrders());
    }

    @GetMapping("/{id}")
    public Optional<Order> getById(@PathVariable String id) {
        return executor.execute(findByIdOrderUseCase, new FindByIdOrderUseCase.InputValues(id),
                output -> output.getOrder());
    }

    @PostMapping("/findByCustomer")
    public Stream<Order> searchByCustomer(@Valid @RequestBody PersonalInfo request) {
        return executor.execute(findOrderByCustomerUseCase, new FindOrderByCustomerUseCase.InputValues(
                request),
                output -> output.getOrders());
    }

    @Value
    private static class CustomerSearchResponse {
        private final PersonalInfo customer;
        private final List<CustomerSearchOrderResponse> orders;
    }

    @Value
    private static class CustomerSearchOrderResponse {
        private final OrderSummary summary;
        private final Address delivery;
    }

}