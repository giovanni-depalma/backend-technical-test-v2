package com.tui.proof.data.db.gateway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.persistence.EntityNotFoundException;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderSummary;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.data.db.entities.OrderData;
import com.tui.proof.data.db.gateway.CustomerGatewayImpl;
import com.tui.proof.data.db.gateway.OrderGatewayImpl;
import com.tui.proof.data.db.mapper.OrderMapper;
import com.tui.proof.data.db.repositories.OrderRepositoryJpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

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
        Order orderToCreate = Mockito.mock(Order.class);
        PersonalInfo customer = Mockito.mock(PersonalInfo.class);
        when(orderToCreate.getCustomer()).thenReturn(customer);
        Order expected = Mockito.mock(Order.class);
        when(mapper.toDomain(any())).thenReturn(expected);
        Order actual = gateway.create(orderToCreate);
        assertEquals(expected, actual);
        verify(customerGateway, times(1)).saveOrUpdate(customer);
    }


    @Test
    public void shouldUpdate() {
        String id = "1";
        Order orderToUpdate = Mockito.mock(Order.class);
        OrderSummary orderSummary = Mockito.mock(OrderSummary.class);
        when(orderToUpdate.getOrderSummary()).thenReturn(orderSummary);
        when(orderSummary.getId()).thenReturn(id);
        PersonalInfo customer = Mockito.mock(PersonalInfo.class);
        when(orderToUpdate.getCustomer()).thenReturn(customer);
        Order expected = Mockito.mock(Order.class);
        when(mapper.toDomain(any())).thenReturn(expected);
        Order actual = gateway.update(orderToUpdate);
        assertEquals(expected, actual);
        verify(customerGateway, times(1)).saveOrUpdate(customer);
    }

    @Test
    public void shouldNotUpdate() {
        String id = "1";
        Order orderToUpdate = Mockito.mock(Order.class);
        OrderSummary orderSummary = Mockito.mock(OrderSummary.class);
        when(orderToUpdate.getOrderSummary()).thenReturn(orderSummary);
        when(orderSummary.getId()).thenReturn(id);
        when(orderRepositoryJpa.getById(any())).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class, () -> {
            gateway.update(orderToUpdate);
        });
    }


    @Test
    public void shouldFindAll() {
        int espectedSize = 10;
        List<OrderData> repositoryMockData = IntStream.range(0,
                espectedSize).mapToObj(i -> Mockito.mock(OrderData.class)).toList();
        Order expectedOrder = Mockito.mock(Order.class); 
        List<Order> expectedOrders = IntStream.range(0,
                espectedSize).mapToObj(i -> expectedOrder).toList();       
        when(orderRepositoryJpa.findAll()).thenReturn(repositoryMockData);
        when(mapper.toDomain(any())).thenReturn(expectedOrder);
        List<Order> actualOrders = gateway.findAll().toList();
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    public void shouldFindById() {
        String id = "1";
        OrderData repositoryMockData = Mockito.mock(OrderData.class);
        Order expectedOrder = Mockito.mock(Order.class);
        when(orderRepositoryJpa.findById(any())).thenReturn(Optional.of(repositoryMockData));
        when(mapper.toDomain(any())).thenReturn(expectedOrder);
        Optional<Order> actual = gateway.findById(id);
        assertEquals(expectedOrder, actual.get());
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
        int espectedSize = 10;
        List<OrderData> repositoryMockData = IntStream.range(0,
                espectedSize).mapToObj(i -> Mockito.mock(OrderData.class)).toList();
        Order expectedOrder = Mockito.mock(Order.class);
        List<Order> expectedOrders = IntStream.range(0,
                espectedSize).mapToObj(i -> expectedOrder).toList();
        when(orderRepositoryJpa.findAll(ArgumentMatchers.<Example<OrderData>>any())).thenReturn(repositoryMockData);
        when(mapper.toDomain(any())).thenReturn(expectedOrder);
        PersonalInfo customer = Mockito.mock(PersonalInfo.class);
        List<Order> actualOrders = gateway.findByCustomer(customer).toList();
        assertEquals(expectedOrders, actualOrders);
    }
}
