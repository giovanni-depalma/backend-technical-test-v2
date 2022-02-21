package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.exception.ServiceException;
import com.tui.proof.mapper.CustomerMapper;
import com.tui.proof.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerService {
    private CustomerRepository customerRepository;
    private CustomerMapper mapper;

    public Flux<Customer> findByExample(Customer customer) {
        try {
            log.debug("findByExample {}", customer);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase();
            return customerRepository.findAll(Example.of(customer, matcher));
        } catch (Exception e) {
            log.error("error findByExample {}", customer,e);
            return Flux.error(new ServiceException());
        }
    }

    public Mono<Customer> findByEmailAndSave(Customer customer) {
        return findByEmail(customer.getEmail()).defaultIfEmpty(new Customer()).map(target -> {
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
