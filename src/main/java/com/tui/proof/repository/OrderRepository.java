package com.tui.proof.repository;

import java.util.Collection;
import java.util.UUID;

import com.tui.proof.domain.entities.Order;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, UUID>, ReactiveQueryByExampleExecutor<Order> {
    @Query("SELECT * FROM order_data WHERE customer IN (:customers)")
    Flux<Order> findByCustomerIn(Collection<UUID> customers);

}
