package com.tui.proof.data.db.repositories;

import java.util.List;

import com.tui.proof.data.db.entities.OrderData;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositoryJpa extends JpaRepository<OrderData, Long> {

    @EntityGraph(value = "graph.order.customer")
    public List<OrderData> findAll();

    @EntityGraph(value = "graph.order.customer")
    public <S extends OrderData> List<S> findAll(Example<S> example);

}
