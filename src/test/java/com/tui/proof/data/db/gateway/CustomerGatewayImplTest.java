package com.tui.proof.data.db.gateway;

import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.data.db.entities.CustomerData;
import com.tui.proof.data.db.mapper.CustomerMapper;
import com.tui.proof.data.db.repositories.CustomerRepositoryJpa;
import com.tui.proof.util.FakeListBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CustomerGatewayImplTest {

    @InjectMocks
    private CustomerGatewayImpl gateway;

    @Mock
    private CustomerRepositoryJpa repository;

    @Mock
    private CustomerMapper mapper;

    @Test
    public void shouldFindAll() {
        int expectedSize = 10;
        List<CustomerData> repositoryMockData = FakeListBuilder.buildList(expectedSize, CustomerData::new);
        Customer expectedCustomer = Customer.builder().build();
        List<Customer> expectedCustomers = FakeListBuilder.buildList(expectedSize, () -> expectedCustomer);
        when(repository.findAll()).thenReturn(repositoryMockData);
        when(mapper.toDomain(any())).thenReturn(expectedCustomer);
        List<Customer> actualCustomers = gateway.findAll().toList();
        assertEquals(expectedCustomers, actualCustomers);
    }

    
    @Test
    public void shouldFindById() {
        CustomerData repositoryMockData = new CustomerData();
        Customer expectedCustomer = Customer.builder().build();
        Long id = 10L;
        when(repository.findById(id)).thenReturn(Optional.of(repositoryMockData));
        when(mapper.toDomain(any())).thenReturn(expectedCustomer);
        Optional<Customer> actual = gateway.findById(id);
        assertEquals(expectedCustomer, actual.orElseThrow());
    }

    @Test
    public void shouldNotFindById() {
        Long id = 10L;
        when(repository.findById(id)).thenReturn(Optional.empty());
        Optional<Customer> actual = gateway.findById(id);
        assertTrue(actual.isEmpty());
    }

    
    @Test
    public void shouldFindByExample() {
        int expectedSize = 10;
        List<CustomerData> repositoryMockData = FakeListBuilder.buildList(expectedSize, CustomerData::new);
        Customer expectedCustomer = Customer.builder().build();
        List<Customer> expectedCustomers = FakeListBuilder.buildList(expectedSize, () -> expectedCustomer);
        when(repository.findAll(ArgumentMatchers.<Example<CustomerData>>any())).thenReturn(repositoryMockData);
        when(mapper.toDomain(any())).thenReturn(expectedCustomer);
        PersonalInfo example = PersonalInfo.builder().build();
        List<Customer> actual = gateway.findByExample(example).toList();
        assertEquals(expectedCustomers, actual);
    }

    @Test
    public void shouldUpdate(){
        PersonalInfo customer = PersonalInfo.builder().build();
        CustomerData already = new CustomerData();
        CustomerData expected = new CustomerData();
        when(repository.findByEmail(any())).thenReturn(Optional.of(already));
        when(repository.save(any())).thenReturn(expected);
        CustomerData actual = gateway.saveOrUpdate(customer);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldInsert() {
        PersonalInfo customer = PersonalInfo.builder().build();
        CustomerData expected = new CustomerData();
        when(repository.findByEmail(any())).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(expected);
        CustomerData actual = gateway.saveOrUpdate(customer);
        assertEquals(expected, actual);
    }

 
}
