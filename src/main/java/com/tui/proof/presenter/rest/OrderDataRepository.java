package com.tui.proof.presenter.rest;

import java.util.List;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.data.db.entities.OrderData;
import com.tui.proof.data.db.entities.OrderData2;

import org.hibernate.type.TrueFalseType;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Test")
@RepositoryRestResource(path = "test", exported = true)
public interface OrderDataRepository extends JpaRepository<OrderData2, Long> {
    
}
