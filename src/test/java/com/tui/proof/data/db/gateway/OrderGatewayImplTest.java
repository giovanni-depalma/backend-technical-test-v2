package com.tui.proof.data.db.gateway;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.data.db.entities.OrderData;
import com.tui.proof.data.db.mapper.OrderMapper;
import com.tui.proof.data.db.repositories.OrderRepositoryJpa;
import com.tui.proof.util.FakeListBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderGatewayImplTest {
    
    @InjectMocks
    private OrderGatewayImpl gateway;

    @Mock
    private CustomerGatewayImpl customerGateway;

    @Mock
    private OrderRepositoryJpa orderRepositoryJpa;

    @Mock
    private OrderMapper mapper;

    @Test
    public void shouldCreate() {
        PersonalInfo customer = PersonalInfo.builder().build();
        Order orderToCreate = Order.builder().customer(customer).build();
        Order expected = Order.builder().build();
        when(mapper.toDomain(any())).thenReturn(expected);
        Order actual = gateway.create(orderToCreate);
        assertEquals(expected, actual);
        verify(customerGateway, times(1)).saveOrUpdate(customer);
    }


    @Test
    public void shouldUpdate() {
        String id = "1";
        PersonalInfo customer = PersonalInfo.builder().build();
        Order orderToUpdate = Order.builder().id(id).customer(customer).build();
        Order expected = Order.builder().build();
        when(mapper.toDomain(any())).thenReturn(expected);
        Order actual = gateway.update(orderToUpdate);
        assertEquals(expected, actual);
        verify(customerGateway, times(1)).saveOrUpdate(customer);
    }

    @Test
    public void shouldNotUpdate() {
        String id = "1";
        Order orderToUpdate = Order.builder().id(id).build();
        when(orderRepositoryJpa.getById(any())).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> gateway.update(orderToUpdate));
    }


    @Test
    public void shouldFindAll() {
        int expectedSize = 10;
        List<OrderData> repositoryMockData = FakeListBuilder.buildList(expectedSize, OrderData::new);
        Order expectedOrder = Order.builder().build();
        List<Order> expectedOrders = FakeListBuilder.buildList(expectedSize, () -> expectedOrder);
        when(orderRepositoryJpa.findAll()).thenReturn(repositoryMockData);
        when(mapper.toDomain(any())).thenReturn(expectedOrder);
        List<Order> actualOrders = gateway.findAll().toList();
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void shouldFindById() {
        String id = "1";
        OrderData repositoryMockData = new OrderData();
        Order expectedOrder = Order.builder().build();
        when(orderRepositoryJpa.findById(any())).thenReturn(Optional.of(repositoryMockData));
        when(mapper.toDomain(any())).thenReturn(expectedOrder);
        Optional<Order> actual = gateway.findById(id);
        assertEquals(expectedOrder, actual.orElseThrow());
    }

    @Test
    public void shouldNotFindById() {
        String id = "1";
        when(orderRepositoryJpa.findById(any())).thenReturn(Optional.empty());
        Optional<Order> actual = gateway.findById(id);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldFindByCustomer() {
        int expectedSize = 10;
        List<OrderData> repositoryMockData = FakeListBuilder.buildList(expectedSize, OrderData::new);
        Order expectedOrder = Order.builder().build();
        List<Order> expectedOrders = FakeListBuilder.buildList(expectedSize, () -> expectedOrder);
        when(orderRepositoryJpa.findAll(ArgumentMatchers.<Example<OrderData>>any())).thenReturn(repositoryMockData);
        when(mapper.toDomain(any())).thenReturn(expectedOrder);
        PersonalInfo customer = PersonalInfo.builder().build();
        List<Order> actualOrders = gateway.findByCustomer(customer).toList();
        assertEquals(expectedOrders, actualOrders);
    }
}
