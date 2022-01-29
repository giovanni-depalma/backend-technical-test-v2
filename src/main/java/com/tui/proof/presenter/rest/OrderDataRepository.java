package com.tui.proof.presenter.rest;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.data.db.entities.OrderData;
import com.tui.proof.data.db.entities.OrderData2;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RepositoryRestResource(path = "test", exported = true)
public interface OrderDataRepository extends CrudRepository<OrderData2, Long> {
    
    @Override
     Iterable<OrderData2> findAll();
}
