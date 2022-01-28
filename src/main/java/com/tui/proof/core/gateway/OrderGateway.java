package com.tui.proof.core.gateway;

import java.util.Optional;
import java.util.stream.Stream;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.PersonalInfo;

public interface OrderGateway {

    Order create(Order order);
    
    Order update(Order order);

    Optional<Order> findById(String id);

    Stream<Order> findByCustomer(PersonalInfo example);

    Stream<Order> findAll();
}
