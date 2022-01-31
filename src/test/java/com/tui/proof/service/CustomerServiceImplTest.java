package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.base.PersonalInfo;
import com.tui.proof.repositories.CustomerRepository;
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
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    public void shouldUpdate(){
        PersonalInfo personalInfo = FakeCustomer.buildPersonalInfo();
        Customer already = new Customer();
        Customer expected = new Customer();
        when(customerRepository.findByPersonalInfo_Email(personalInfo.getEmail())).thenReturn(Optional.of(already));
        when(customerRepository.save(any())).thenReturn(expected);
        Customer actual = customerService.findByEmailAndSave(personalInfo);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldInsert() {
        PersonalInfo personalInfo = FakeCustomer.buildPersonalInfo();
        Customer expected = new Customer();
        when(customerRepository.findByPersonalInfo_Email(personalInfo.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenReturn(expected);
        Customer actual = customerService.findByEmailAndSave(personalInfo);
        assertEquals(expected, actual);
    }
}
