package com.tui.proof.service;

import com.tui.proof.repositories.Customer2RepositoryJpa;
import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.PersonalInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final Customer2RepositoryJpa customerRepository;

    @Override
    public Customer findByEmailAndSave(PersonalInfo personalInfo) {
        log.debug("findByEmailAndSave order: {}", personalInfo);
        Optional<Customer> found = customerRepository.findByPersonalInfo_Email(personalInfo.getEmail());
        Customer toSave = found.orElseGet(Customer::new);
        toSave.setPersonalInfo(personalInfo);
        return customerRepository.save(toSave);
    }
}
