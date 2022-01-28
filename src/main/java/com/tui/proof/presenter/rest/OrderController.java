package com.tui.proof.presenter.rest;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderRequest;

import com.tui.proof.core.service.OrderCreator;
import com.tui.proof.core.service.OrderUpdater;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/public/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderCreator orderCreator;
    private final OrderUpdater orderUpdater;

    @GetMapping
    public String a() {
        return "ok";
    }

    @PostMapping
    public Order create(@Valid @RequestBody OrderRequest request) {
        return orderCreator.createOrder(request);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable String id, @Valid @RequestBody OrderRequest request) {
        return orderUpdater.updateOrder(id, request);
    }

}
