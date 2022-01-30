package com.tui.proof.old.core.gateway;

import java.util.Optional;
import java.util.stream.Stream;

import com.tui.proof.old.OrderOld;
import com.tui.proof.domain.entities.PersonalInfo;

public interface OrderGateway {

    OrderOld create(OrderOld order);
    
    OrderOld update(OrderOld order);

    Optional<OrderOld> findById(String id);

    Stream<OrderOld> findByCustomer(PersonalInfo example);

    Stream<OrderOld> findAll();
}
