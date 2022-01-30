package com.tui.proof.service;

import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.repositories.OrderRepository;
import com.tui.proof.util.FakeCustomer;
import com.tui.proof.util.FakeListBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminOrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private AdminOrderServiceImpl orderService;

    @Test
    public void shouldFindByCustomer() {
        PersonalInfo customer = FakeCustomer.buildPersonalInfo();
        List<Order> expected = FakeListBuilder.buildList(Order::new);
        when(orderRepository.findAll(ArgumentMatchers.<Example<Order>>any())).thenReturn(expected);
        List<Order> actual = orderService.findByCustomer(customer);
        assertEquals(expected, actual);
    }

}
