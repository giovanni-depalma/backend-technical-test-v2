package com.tui.proof.data.db.gateway;

import java.util.Optional;
import java.util.stream.Stream;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.core.gateway.OrderGateway;
import com.tui.proof.data.db.entities.CustomerData;
import com.tui.proof.data.db.entities.OrderData;
import com.tui.proof.data.db.mapper.OrderMapper;
import com.tui.proof.data.db.repositories.OrderRepositoryJpa;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class OrderGatewayImpl implements OrderGateway {

    private final OrderRepositoryJpa orderRepositoryJpa;
    private final CustomerGatewayImpl customerGateway;
    private final OrderMapper mapper;

    @Override
    public Order create(Order order) {
        CustomerData customer = customerGateway.saveOrUpdate(order.getCustomer());
        OrderData toSave = new OrderData();
        mapper.populateData(toSave, order, customer);
        OrderData saved = orderRepositoryJpa.save(toSave);
        return mapper.toDomain(saved);
    }

    @Override
    public Order update(Order order) {
        Long id = Long.parseLong(order.getOrderSummary().getId());
        OrderData toUpdate = orderRepositoryJpa.getById(id);
        CustomerData customer = customerGateway.saveOrUpdate(order.getCustomer());
        mapper.populateData(toUpdate, order, customer);
        OrderData saved = orderRepositoryJpa.save(toUpdate);
        return mapper.toDomain(saved);
    }

    @Override
    public Stream<Order> findAll() {
        return orderRepositoryJpa.findAll().stream().map(mapper::toDomain);
    }

    @Override
    public Optional<Order> findById(String id) {
        return orderRepositoryJpa.findById(Long.parseLong(id)).map(mapper::toDomain);
    }

    @Override
    public Stream<Order> findByCustomer(PersonalInfo example) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(StringMatcher.CONTAINING).withIgnoreCase();
        OrderData orderData = new OrderData();
        orderData.setCustomer(customerGateway.customerDataFromExample(example));
        return orderRepositoryJpa.findAll(Example.of(orderData, matcher)).stream()
                .map(mapper::toDomain);
    }

}
