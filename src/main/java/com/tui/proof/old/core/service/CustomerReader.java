package com.tui.proof.old.core.service;

import com.tui.proof.old.CustomerOld;
import com.tui.proof.domain.entities.PersonalInfo;

import java.util.Optional;
import java.util.stream.Stream;

public interface CustomerReader {

    Stream<CustomerOld> findAll();

    Optional<CustomerOld> findById(long id);

    Stream<CustomerOld> findByExample(PersonalInfo request);
}
