package com.tui.proof.presenter.rest;

import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.core.domain.exception.ItemNotFoundException;
import com.tui.proof.core.service.CustomerReader;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.stream.Stream;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/admin/customers")
@SecurityRequirement(name = "secure-api")
@AllArgsConstructor
public class AdminCustomerController {

    private final CustomerReader customerReader;

    @GetMapping
    public Stream<Customer> findAll() {
        return customerReader.findAll();
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable long id) {
        return customerReader.findById(id).orElseThrow(ItemNotFoundException::new);
    }

    @PostMapping("/findByExample")
    public Stream<Customer> searchByExample(@RequestBody PersonalInfo example) {
        return customerReader.findByExample(example);
    }

}