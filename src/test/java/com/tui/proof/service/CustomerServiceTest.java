package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.exception.ServiceException;
import com.tui.proof.mapper.CustomerMapper;
import com.tui.proof.repository.CustomerRepository;
import com.tui.proof.util.FakeCustomer;
import com.tui.proof.util.FakeListBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

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


    @Test
    public void shouldFindByExample() {
        Customer example = new Customer();
        List<Customer> expected = FakeListBuilder.buildList(Customer::new);
        when(customerRepository.findAll(ArgumentMatchers.any())).thenReturn(Flux.fromIterable(expected));
        Flux<Customer> actual = customerService.findByExample(example);
        StepVerifier.create(actual).expectNext(expected.toArray(new Customer[0])).verifyComplete();
    }


    @Test
    public void shouldNotFindByCustomerWhenError() {
        Flux<Customer> actual = customerService.findByExample(null);
        StepVerifier.create(actual).expectError(ServiceException.class).verify();
    }
}
