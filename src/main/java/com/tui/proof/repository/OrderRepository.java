package com.tui.proof.repository;

import java.util.List;
import java.util.UUID;

import com.tui.proof.domain.entities.Order;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Override
    @EntityGraph(value = "graph.order.customer")
    <S extends Order> List<S> findAll(Example<S> example);

    @Override
    @RestResource(exported = false)
    <S extends Order> S save(S entity);

}
