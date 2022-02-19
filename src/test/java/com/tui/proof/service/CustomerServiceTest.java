package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.mapper.CustomerMapper;
import com.tui.proof.repository.CustomerRepository;
import com.tui.proof.util.FakeCustomer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void shouldFind(){
        Customer customer = FakeCustomer.buildCustomer();
        Customer expected = new Customer();
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Mono.just(expected));
        Mono<Customer> actual = customerService.findByEmail(customer.getEmail());
        StepVerifier.create(actual).expectNext(expected).verifyComplete();
    }

    @Test
    public void shouldNotFind(){
        Customer customer = FakeCustomer.buildCustomer();
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Mono.empty());
        Mono<Customer> actual = customerService.findByEmail(customer.getEmail());
        StepVerifier.create(actual).verifyComplete();
    }

    @Test
    public void shouldSave() {
        Customer expected = FakeCustomer.buildCustomer();
        when(customerRepository.save(any())).thenReturn(Mono.just(expected));
        Mono<Customer> actual = customerService.save(expected);
        StepVerifier.create(actual).expectNext(expected).verifyComplete();
    }

    @Test
    public void shouldNotFindByMailAndSave() {
        Customer expected = FakeCustomer.buildCustomer();
        when(customerRepository.findByEmail(expected.getEmail())).thenReturn(Mono.empty());
        when(customerRepository.save(any())).thenReturn(Mono.just(expected));
        Mono<Customer> actual = customerService.findByEmailAndSave(expected);
        StepVerifier.create(actual).expectNext(expected).verifyComplete();
    }

    @Test
    public void shouldFindByMailAndSave() {
        Customer expected = FakeCustomer.buildCustomer();
        when(customerRepository.findByEmail(expected.getEmail())).thenReturn(Mono.just(expected));
        when(customerRepository.save(any())).thenReturn(Mono.just(expected));
        Mono<Customer> actual = customerService.findByEmailAndSave(expected);
        StepVerifier.create(actual).expectNext(expected).verifyComplete();
    }
}
