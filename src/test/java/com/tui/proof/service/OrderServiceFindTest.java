package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.exception.ServiceException;
import com.tui.proof.repository.OrderRepository;
import com.tui.proof.util.FakeCustomer;
import com.tui.proof.util.FakeListBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceFindTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void shouldFindByCustomer() {
        Customer customer = FakeCustomer.buildCustomer();
        List<Order> expected = FakeListBuilder.buildList(Order::new);
        when(orderRepository.findAll(ArgumentMatchers.<Example<Order>>any())).thenReturn(Flux.fromIterable(expected));
        Flux<Order> actual = orderService.findByCustomer(customer);
        StepVerifier.create(actual).expectNext(expected.toArray(new Order[expected.size()])).verifyComplete();
    }

    @Test
    public void shouldNotFindByCustomerWhenTrows() {
        when(orderRepository.findAll(ArgumentMatchers.<Example<Order>>any())).thenThrow(new RuntimeException());
        Flux<Order> actual = orderService.findByCustomer(null);
        StepVerifier.create(actual).expectError(ServiceException.class).verify();
    }

    @Test
    public void shouldNotFindByCustomerWhenError() {
        when(orderRepository.findAll(ArgumentMatchers.<Example<Order>>any())).thenReturn(Flux.error(new RuntimeException()));
        Flux<Order> actual = orderService.findByCustomer(null);
        StepVerifier.create(actual).expectError(ServiceException.class).verify();
    }
}
