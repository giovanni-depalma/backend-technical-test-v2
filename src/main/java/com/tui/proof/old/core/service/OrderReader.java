package com.tui.proof.old.core.service;

import com.tui.proof.old.OrderOld;
import com.tui.proof.domain.entities.PersonalInfo;

import java.util.Optional;
import java.util.stream.Stream;

public interface OrderReader {

    Optional<OrderOld> findById(String id);

    Stream<OrderOld> findAll();

    Stream<OrderOld> findByCustomer(PersonalInfo customer);
}
