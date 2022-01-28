package com.tui.proof.core.service;

import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.core.gateway.CustomerGateway;
import com.tui.proof.util.FakeCustomer;
import com.tui.proof.util.FakeListBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerReaderImplTest {

    @InjectMocks
    private CustomerReaderImpl reader;

    @Mock
    private CustomerGateway customerGateway;

    @Test
    public void shouldFindAll() {
        List<Customer> expected = FakeListBuilder.buildList(() -> Customer.builder().build());
        when(customerGateway.findAll()).thenReturn(expected.stream());
        List<Customer> actual = reader.findAll().toList();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindById() {
        long id = 1;
        Customer expected = Customer.builder().build();
        when(customerGateway.findById(any())).thenReturn(Optional.of(expected));
        Customer actual = reader.findById(id).orElseThrow();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotFindById() {
        long id = 1;
        when(customerGateway.findById(any())).thenReturn(Optional.empty());
        Optional<Customer> actual = reader.findById(id);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldFindByExample() {
        List<Customer> expected = FakeListBuilder.buildList(() -> Customer.builder().build());
        when(customerGateway.findByExample(any())).thenReturn(expected.stream());
        PersonalInfo example = FakeCustomer.buildPersonalInfo();
        List<Customer> actual = reader.findByExample(example).toList();
        assertEquals(expected, actual);
    }
}
