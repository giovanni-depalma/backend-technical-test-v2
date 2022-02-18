package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.mapper.CustomerMapper;
import com.tui.proof.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerService {
    private CustomerRepository customerRepository;
    private CustomerMapper mapper;

    private Customer newCustomer(){
        Customer c = new Customer();
        c.setId(UUID.randomUUID());
        return c;
    }

    public Mono<Customer> findByEmailAndSave(Customer customer) {
        return findByEmail(customer.getEmail()).defaultIfEmpty(newCustomer()).map(target -> {
            mapper.update(customer, target);
            return target;
        }).flatMap(this::save);
    }

    public Mono<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Mono<Customer> save(Customer customer) {
        return customerRepository.save(customer);
    }
}
