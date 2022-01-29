package com.tui.proof.presenter.rest;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.core.domain.exception.ItemNotFoundException;
import com.tui.proof.core.service.OrderReader;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.stream.Stream;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/orders")
@SecurityRequirement(name = "secure-api")
@SecurityRequirement(name = "secure-api2")
@AllArgsConstructor
public class AdminOrderController {
    private final OrderReader orderReader;

    @GetMapping
    public Stream<Order> findAll() {
        return orderReader.findAll();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable String id) {
        return orderReader.findById(id).orElseThrow(ItemNotFoundException::new);
    }

    @PostMapping("/findByCustomer")
    public Stream<Order> findByCustomer(@RequestBody PersonalInfo request) {
        return orderReader.findByCustomer(request);
    }

}