package com.tui.proof.old;

import java.util.List;

import com.tui.proof.old.db.entities.OrderDataOld;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepositoryJpa extends JpaRepository<OrderDataOld, Long> {

    @EntityGraph(value = "graph.orderOld.customer")
    List<OrderDataOld> findAll();

    @EntityGraph(value = "graph.orderOld.customer")
    <S extends OrderDataOld> List<S> findAll(Example<S> example);

}
