package com.tui.proof.old.core.service;

import com.tui.proof.old.CustomerOld;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.old.core.gateway.CustomerGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerReaderImpl implements CustomerReader {
    private final CustomerGateway customerGateway;

    @Override
    public Stream<CustomerOld> findAll() {
        log.debug("findAll");
        return customerGateway.findAll();
    }

    @Override
    public Optional<CustomerOld> findById(long id) {
        log.debug("find customer by id {}", id);
        return customerGateway.findById(id);
    }

    @Override
    public Stream<CustomerOld> findByExample(PersonalInfo example) {
        log.debug("find customer by example {}", example);
        return customerGateway.findByExample(example);
    }


}
