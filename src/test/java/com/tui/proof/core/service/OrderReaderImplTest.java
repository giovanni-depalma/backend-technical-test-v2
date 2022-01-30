package com.tui.proof.core.service;

import com.tui.proof.old.OrderOld;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.old.core.gateway.OrderGateway;
import com.tui.proof.old.core.service.OrderReaderImpl;
import com.tui.proof.util.FakeCustomer;
import com.tui.proof.util.FakeListBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderReaderImplTest {

    @InjectMocks
    private OrderReaderImpl reader;

    @Mock
    private OrderGateway orderGateway;

    @Test
    public void shouldFindAll() {
        List<OrderOld> expected = FakeListBuilder.buildList(()-> OrderOld.builder().build());
        when(orderGateway.findAll()).thenReturn(expected.stream());
        List<OrderOld> actual = reader.findAll().toList();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindById() {
        String id = "1";
        OrderOld expected = OrderOld.builder().build();
        when(orderGateway.findById(any())).thenReturn(Optional.of(expected));
        OrderOld actual = reader.findById(id).orElseThrow();
        assertEquals(expected, actual);
    }

    @Test
    public void shouldNotFindById() {
        String id = "1";
        when(orderGateway.findById(any())).thenReturn(Optional.empty());
        Optional<OrderOld> actual = reader.findById(id);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldFindByCustomer() {
        PersonalInfo customer = FakeCustomer.buildPersonalInfo();
        List<OrderOld> expected = FakeListBuilder.buildList(()-> OrderOld.builder().build());
        when(orderGateway.findByCustomer(customer)).thenReturn(expected.stream());
        List<OrderOld> actual = reader.findByCustomer(customer).toList();
        assertEquals(expected, actual);
    }
}
