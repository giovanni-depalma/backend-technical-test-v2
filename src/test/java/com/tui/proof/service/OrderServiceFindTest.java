package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.exception.ServiceException;
import com.tui.proof.repository.OrderRepository;
import com.tui.proof.util.FakeCustomer;
import com.tui.proof.util.FakeListBuilder;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceFindTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void shouldFindByCustomer() {
        Customer customer = FakeCustomer.buildCustomer();
        List<Customer> expectedCustomers = FakeListBuilder.buildList(Customer::new);
        List<Order> expected = FakeListBuilder.buildList(() -> FakeOrder.buildOrder());
        when(customerService.findByExample(ArgumentMatchers.any())).thenReturn(Flux.fromIterable(expectedCustomers));
        when(orderRepository.findByCustomerIn(any())).thenReturn(Flux.fromIterable(expected));
        Flux<Order> actual = orderService.findByCustomer(customer);
        StepVerifier.create(actual).expectNext(expected.toArray(new Order[0])).verifyComplete();
    }

    @Test
    public void shouldFindByCustomerEmpty() {
        Customer customer = FakeCustomer.buildCustomer();
        when(customerService.findByExample(ArgumentMatchers.any())).thenReturn(Flux.empty());
        Flux<Order> actual = orderService.findByCustomer(customer);
        StepVerifier.create(actual).verifyComplete();
    }

    @Test
    public void shouldNotFindByCustomerWhenError() {
        Customer customer = FakeCustomer.buildCustomer();
        when(customerService.findByExample(ArgumentMatchers.any())).thenReturn(Flux.error(new ServiceException()));
        Flux<Order> actual = orderService.findByCustomer(customer);
        StepVerifier.create(actual).expectError(ServiceException.class).verify();
    }
}
