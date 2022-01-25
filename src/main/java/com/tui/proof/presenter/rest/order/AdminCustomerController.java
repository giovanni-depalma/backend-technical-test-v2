package com.tui.proof.presenter.rest.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.Valid;

import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.core.usecases.UseCaseExecutor;
import com.tui.proof.core.usecases.customer.FindAllCustomerUseCase;
import com.tui.proof.core.usecases.customer.FindByIdCustomerUseCase;
import com.tui.proof.core.usecases.customer.FindCustomerByExample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin/customers")
@AllArgsConstructor
public class AdminCustomerController {

    private final UseCaseExecutor executor;
    private final FindAllCustomerUseCase findAllCustomerUseCase;
    private final FindByIdCustomerUseCase findByIdCustomerUseCase;
    private final FindCustomerByExample findCustomerByExample;

    @GetMapping
    public List<Customer> findAll() {
        return executor.execute(findAllCustomerUseCase, new FindAllCustomerUseCase.InputValues(),
                output -> output.getCustomers());
    }

    @GetMapping("/{id}")
    public Optional<Customer> getById(@PathVariable long id) {
        return executor.execute(
                findByIdCustomerUseCase, new FindByIdCustomerUseCase.InputValues(id),
                output -> output.getCustomer());
    }

    @PostMapping("/findByExample")
    public Stream<Customer> searchByExample(@Valid @RequestBody PersonalInfo request) {
        return executor.execute(
                findCustomerByExample, new FindCustomerByExample.InputValues(request),
                output -> output.getCustomers());
    }

}