package com.tui.proof.old.db.gateway;

import java.util.Optional;
import java.util.stream.Stream;

import com.tui.proof.old.OrderOld;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.old.core.gateway.OrderGateway;
import com.tui.proof.old.db.entities.CustomerDataOld;
import com.tui.proof.old.db.entities.OrderDataOld;
import com.tui.proof.old.db.mapper.OrderMapper;
import com.tui.proof.old.OrderRepositoryJpa;

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
    public OrderOld create(OrderOld order) {
        CustomerDataOld customer = customerGateway.saveOrUpdate(order.getCustomer());
        OrderDataOld toSave = new OrderDataOld();
        mapper.populateData(toSave, order, customer);
        OrderDataOld saved = orderRepositoryJpa.save(toSave);
        return mapper.toDomain(saved);
    }

    @Override
    public OrderOld update(OrderOld order) {
        Long id = Long.parseLong(order.getId());
        OrderDataOld toUpdate = orderRepositoryJpa.getById(id);
        CustomerDataOld customer = customerGateway.saveOrUpdate(order.getCustomer());
        mapper.populateData(toUpdate, order, customer);
        OrderDataOld saved = orderRepositoryJpa.save(toUpdate);
        return mapper.toDomain(saved);
    }

    @Override
    public Stream<OrderOld> findAll() {
        return orderRepositoryJpa.findAll().stream().map(mapper::toDomain);
    }

    @Override
    public Optional<OrderOld> findById(String id) {
        return orderRepositoryJpa.findById(Long.parseLong(id)).map(mapper::toDomain);
    }

    @Override
    public Stream<OrderOld> findByCustomer(PersonalInfo example) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(StringMatcher.CONTAINING).withIgnoreCase();
        OrderDataOld orderData = new OrderDataOld();
        orderData.setCustomer(customerGateway.customerDataFromExample(example));
        return orderRepositoryJpa.findAll(Example.of(orderData, matcher)).stream()
                .map(mapper::toDomain);
    }

}
