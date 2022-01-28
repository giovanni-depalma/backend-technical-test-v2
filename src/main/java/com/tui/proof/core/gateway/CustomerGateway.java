package com.tui.proof.core.gateway;

import java.util.Optional;
import java.util.stream.Stream;

import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.PersonalInfo;

public interface CustomerGateway {

    Optional<Customer> findById(Long id);

    Stream<Customer> findAll();

    Stream<Customer> findByExample(PersonalInfo example);
}
