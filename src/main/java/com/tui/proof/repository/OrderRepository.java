package com.tui.proof.repository;

import java.util.List;
import java.util.UUID;

import com.tui.proof.domain.entities.Order;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, UUID>, ReactiveQueryByExampleExecutor<Order> {


}
