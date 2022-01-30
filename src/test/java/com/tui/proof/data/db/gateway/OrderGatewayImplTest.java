package com.tui.proof.data.db.gateway;

import com.tui.proof.old.OrderOld;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.old.db.entities.OrderDataOld;
import com.tui.proof.old.db.mapper.OrderMapper;
import com.tui.proof.old.OrderRepositoryJpa;
import com.tui.proof.old.db.gateway.CustomerGatewayImpl;
import com.tui.proof.old.db.gateway.OrderGatewayImpl;
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
        OrderOld orderToCreate = OrderOld.builder().customer(customer).build();
        OrderOld expected = OrderOld.builder().build();
        when(mapper.toDomain(any())).thenReturn(expected);
        OrderOld actual = gateway.create(orderToCreate);
        assertEquals(expected, actual);
        verify(customerGateway, times(1)).saveOrUpdate(customer);
    }


    @Test
    public void shouldUpdate() {
        String id = "1";
        PersonalInfo customer = PersonalInfo.builder().build();
        OrderOld orderToUpdate = OrderOld.builder().id(id).customer(customer).build();
        OrderOld expected = OrderOld.builder().build();
        when(mapper.toDomain(any())).thenReturn(expected);
        OrderOld actual = gateway.update(orderToUpdate);
        assertEquals(expected, actual);
        verify(customerGateway, times(1)).saveOrUpdate(customer);
    }

    @Test
    public void shouldNotUpdate() {
        String id = "1";
        OrderOld orderToUpdate = OrderOld.builder().id(id).build();
        when(orderRepositoryJpa.getById(any())).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> gateway.update(orderToUpdate));
    }


    @Test
    public void shouldFindAll() {
        int expectedSize = 10;
        List<OrderDataOld> repositoryMockData = FakeListBuilder.buildList(expectedSize, OrderDataOld::new);
        OrderOld expectedOrder = OrderOld.builder().build();
        List<OrderOld> expectedOrders = FakeListBuilder.buildList(expectedSize, () -> expectedOrder);
        when(orderRepositoryJpa.findAll()).thenReturn(repositoryMockData);
        when(mapper.toDomain(any())).thenReturn(expectedOrder);
        List<OrderOld> actualOrders = gateway.findAll().toList();
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void shouldFindById() {
        String id = "1";
        OrderDataOld repositoryMockData = new OrderDataOld();
        OrderOld expectedOrder = OrderOld.builder().build();
        when(orderRepositoryJpa.findById(any())).thenReturn(Optional.of(repositoryMockData));
        when(mapper.toDomain(any())).thenReturn(expectedOrder);
        Optional<OrderOld> actual = gateway.findById(id);
        assertEquals(expectedOrder, actual.orElseThrow());
    }

    @Test
    public void shouldNotFindById() {
        String id = "1";
        when(orderRepositoryJpa.findById(any())).thenReturn(Optional.empty());
        Optional<OrderOld> actual = gateway.findById(id);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldFindByCustomer() {
        int expectedSize = 10;
        List<OrderDataOld> repositoryMockData = FakeListBuilder.buildList(expectedSize, OrderDataOld::new);
        OrderOld expectedOrder = OrderOld.builder().build();
        List<OrderOld> expectedOrders = FakeListBuilder.buildList(expectedSize, () -> expectedOrder);
        when(orderRepositoryJpa.findAll(ArgumentMatchers.<Example<OrderDataOld>>any())).thenReturn(repositoryMockData);
        when(mapper.toDomain(any())).thenReturn(expectedOrder);
        PersonalInfo customer = PersonalInfo.builder().build();
        List<OrderOld> actualOrders = gateway.findByCustomer(customer).toList();
        assertEquals(expectedOrders, actualOrders);
    }
}
