package com.tui.proof.repositories;

import java.util.List;
import java.util.UUID;

import com.tui.proof.domain.entities.Order;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@Tag(name = "Order", description = "Endpoints for Admin to view and manage orders")
@SecurityRequirement(name = "secure-api")
@SecurityRequirement(name = "secure-api2")
@RepositoryRestResource(path = "orders")
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Override
    @EntityGraph(value = "graph.order.customer")
    List<Order> findAll();

    @Override
    @EntityGraph(value = "graph.order.customer")
    List<Order> findAll(Sort sort);

    @Override
    @EntityGraph(value = "graph.order.customer")
    Page<Order> findAll(Pageable pageable);

    @Override
    @EntityGraph(value = "graph.order.customer")
    <S extends Order> List<S> findAll(Example<S> example);

    @Override
    @RestResource(exported = false)
    <S extends Order> S save(S entity);

    @Override
    @RestResource(exported = false)
    void delete(Order entity);
}
