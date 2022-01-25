package com.tui.proof.core.gateway;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.PersonalInfo;

public interface OrderGateway {

    public Order create(Order order);
    
    public Order update(Order order);

    public Optional<Order> findById(String id);

    public Stream<Order> findByCustomer(PersonalInfo example);

    public Stream<Order> findAll();
}
