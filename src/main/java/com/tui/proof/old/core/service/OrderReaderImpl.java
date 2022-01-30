package com.tui.proof.old.core.service;

import com.tui.proof.old.OrderOld;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.old.core.gateway.OrderGateway;
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
    public Optional<OrderOld> findById(String id) {
        log.debug("find customer with by id {}", id);
        return orderGateway.findById(id);
    }

    @Override
    public Stream<OrderOld> findAll() {
        log.debug("find all");
        return orderGateway.findAll();
    }

    @Override
    public Stream<OrderOld> findByCustomer(PersonalInfo customer) {
        log.debug("find customer with by customer {}", customer);
        return orderGateway.findByCustomer(customer);
    }
}
