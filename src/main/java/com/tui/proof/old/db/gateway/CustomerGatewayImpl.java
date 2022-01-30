package com.tui.proof.old.db.gateway;

import java.util.Optional;
import java.util.stream.Stream;

import com.tui.proof.old.CustomerOld;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.old.core.gateway.CustomerGateway;
import com.tui.proof.old.db.entities.CustomerDataOld;
import com.tui.proof.old.db.mapper.CustomerMapper;

import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class CustomerGatewayImpl implements CustomerGateway {

    //private final CustomerRepositoryJpa customerRepositoryJpa;

    private final CustomerMapper mapper;

    @Override
    public Stream<CustomerOld> findAll() {
        return null;
        //return customerRepositoryJpa.findAll().stream().map(mapper::toDomain);
    }

    @Override
    public Optional<CustomerOld> findById(Long id) {
        return null;
        //return customerRepositoryJpa.findById(UUID.fromString("id")).map(mapper::toDomain);
    }

    @Override
    public Stream<CustomerOld> findByExample(PersonalInfo example) {
        return null;
        /*
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(StringMatcher.CONTAINING).withIgnoreCase();
        CustomerData customerData = customerDataFromExample(example);
        return customerRepositoryJpa.findAll(Example.of(customerData, matcher)).stream().map(mapper::toDomain);

         */
    }

    public CustomerDataOld saveOrUpdate(PersonalInfo customer) {
        return null;
        /*
        Optional<CustomerData> found = customerRepositoryJpa.findByEmail(customer.getEmail());
        CustomerData toSave = found.orElseGet(CustomerData::new);
        mapper.populateData(toSave, customer);
        return customerRepositoryJpa.save(toSave);

         */
    }

    public CustomerDataOld customerDataFromExample(PersonalInfo example){
        return null;
        /*
        CustomerData customerData = new CustomerData();
        mapper.populateData(customerData, example);
        return customerData;

         */
    }

}
