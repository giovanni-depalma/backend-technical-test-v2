package com.tui.proof.core.service;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.core.gateway.OrderGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class OrderReaderImpl implements OrderReader{
    private final OrderGateway orderGateway;

    @Override
    public Optional<Order> findById(String id) {
        log.debug("find customer with by id {}", id);
        return orderGateway.findById(id);
    }

    @Override
    public Stream<Order> findAll() {
        log.debug("find all");
        return orderGateway.findAll();
    }

    @Override
    public Stream<Order> findByCustomer(PersonalInfo customer) {
        log.debug("find customer with by customer {}", customer);
        return orderGateway.findByCustomer(customer);
    }
}
