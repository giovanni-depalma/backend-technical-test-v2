package com.tui.proof.core.service;

import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.core.gateway.CustomerGateway;
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
    public Stream<Customer> findAll() {
        log.debug("findAll");
        return customerGateway.findAll();
    }

    @Override
    public Optional<Customer> findById(long id) {
        log.debug("find customer by id {}", id);
        return customerGateway.findById(id);
    }

    @Override
    public Stream<Customer> findByExample(PersonalInfo example) {
        log.debug("find customer by example {}", example);
        return customerGateway.findByExample(example);
    }


}
