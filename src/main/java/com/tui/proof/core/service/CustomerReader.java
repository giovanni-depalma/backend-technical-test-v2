package com.tui.proof.core.service;

import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.PersonalInfo;

import java.util.Optional;
import java.util.stream.Stream;

public interface CustomerReader {

    Stream<Customer> findAll();

    Optional<Customer> findById(long id);

    Stream<Customer> findByExample(PersonalInfo request);
}
