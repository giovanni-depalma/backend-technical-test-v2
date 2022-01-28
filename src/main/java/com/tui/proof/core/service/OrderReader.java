package com.tui.proof.core.service;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.PersonalInfo;

import java.util.Optional;
import java.util.stream.Stream;

public interface OrderReader {

    Optional<Order> findById(String id);

    Stream<Order> findAll();

    Stream<Order> findByCustomer(PersonalInfo customer);
}
