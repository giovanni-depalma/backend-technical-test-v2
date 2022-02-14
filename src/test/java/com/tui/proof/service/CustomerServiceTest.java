package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.repository.CustomerRepository;
import com.tui.proof.util.FakeCustomer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void shouldUpdate(){
        Customer customer = FakeCustomer.buildCustomer();
        Customer already = new Customer();
        Customer expected = new Customer();
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(already));
        when(customerRepository.save(any())).thenReturn(expected);
        Customer actual = customerService.findByEmailAndSave(customer);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldInsert() {
        Customer customer = FakeCustomer.buildCustomer();
        Customer expected = new Customer();
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenReturn(expected);
        Customer actual = customerService.findByEmailAndSave(customer);
        assertEquals(expected, actual);
    }
}
