package com.tui.proof.data.db.gateway;

import java.util.Optional;
import java.util.stream.Stream;

import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.core.gateway.CustomerGateway;
import com.tui.proof.data.db.entities.CustomerData;
import com.tui.proof.data.db.mapper.CustomerMapper;
import com.tui.proof.data.db.repositories.CustomerRepositoryJpa;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CustomerGatewayImpl implements CustomerGateway {

    private final CustomerRepositoryJpa customerRepositoryJpa;

    private final CustomerMapper mapper;

    @Override
    public Stream<Customer> findAll() {
        return customerRepositoryJpa.findAll().stream().map(mapper::toDomain);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepositoryJpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public Stream<Customer> findByExample(PersonalInfo example) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(StringMatcher.CONTAINING).withIgnoreCase();
        CustomerData customerData = customerDataFromExample(example);
        return customerRepositoryJpa.findAll(Example.of(customerData, matcher)).stream().map(mapper::toDomain);
    }

    public CustomerData saveOrUpdate(PersonalInfo customer) {
        Optional<CustomerData> found = customerRepositoryJpa.findByEmail(customer.getEmail());
        CustomerData toSave = found.orElseGet(CustomerData::new);
        mapper.populateData(toSave, customer);
        return customerRepositoryJpa.save(toSave);
    }

    public CustomerData customerDataFromExample(PersonalInfo example){
        CustomerData customerData = new CustomerData();
        mapper.populateData(customerData, example);
        return customerData;
    }

}
