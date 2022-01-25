package com.tui.proof.presenter.rest.order;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderRequest;
import com.tui.proof.core.usecases.UseCaseExecutor;
import com.tui.proof.core.usecases.order.CreateOrderUseCase;
import com.tui.proof.core.usecases.order.UpdateOrderUseCase;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class CustomerOrderController {

    private final UseCaseExecutor executor;
    private final CreateOrderUseCase createOrderUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;

    @PostMapping
    public Order create(@Valid @RequestBody OrderRequest request) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(request);
        for (ConstraintViolation<OrderRequest> violation : violations) {
            System.out.println((violation.getMessage()));
        }
        return executor.execute(createOrderUseCase, new CreateOrderUseCase.InputValues(
                request), output -> output.getOrder());
    }

    @PutMapping("/{id}")
    public Optional<Order> update(@PathVariable String id, @Valid @RequestBody OrderRequest request) {
        return executor.execute(
                updateOrderUseCase, new UpdateOrderUseCase.InputValues(id, request), output -> output.getOrder());
    }

    @DeleteMapping("/{id}")
    public Order delete(@PathVariable String id) {
        System.out.println(id);
        return null;
    }

}
