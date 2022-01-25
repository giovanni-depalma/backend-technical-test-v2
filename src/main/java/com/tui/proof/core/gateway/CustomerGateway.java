package com.tui.proof.core.gateway;

import java.util.Optional;
import java.util.stream.Stream;

import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.PersonalInfo;

public interface CustomerGateway {

    public Optional<Customer> findById(Long id);

    public Stream<Customer> findAll();

    public Stream<Customer> findByExample(PersonalInfo example);
}
