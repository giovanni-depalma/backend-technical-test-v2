package com.tui.proof.data.db.repositories;

import java.util.List;

import com.tui.proof.data.db.entities.OrderData;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


public interface OrderRepositoryJpa extends JpaRepository<OrderData, Long> {

    @EntityGraph(value = "graph.order.customer")
    List<OrderData> findAll();

    @EntityGraph(value = "graph.order.customer")
    <S extends OrderData> List<S> findAll(Example<S> example);

}
