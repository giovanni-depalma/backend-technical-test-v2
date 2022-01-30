package com.tui.proof.old.core.gateway;


import com.tui.proof.old.CustomerOld;
import com.tui.proof.domain.entities.PersonalInfo;

import java.util.Optional;
import java.util.stream.Stream;

public interface CustomerGateway {

    Optional<CustomerOld> findById(Long id);

    Stream<CustomerOld> findAll();

    Stream<CustomerOld> findByExample(PersonalInfo example);
}
