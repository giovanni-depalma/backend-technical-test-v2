package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.base.PersonalInfo;
import com.tui.proof.domain.exception.ServiceException;
import com.tui.proof.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer findByEmailAndSave(PersonalInfo personalInfo) {
        try{
            log.debug("findByEmailAndSave order: {}", personalInfo);
            Optional<Customer> found = customerRepository.findByPersonalInfo_Email(personalInfo.getEmail());
            Customer toSave = found.orElseGet(Customer::new);
            toSave.setPersonalInfo(personalInfo);
            return customerRepository.save(toSave);
        }
        catch(Exception e){
            log.error("error findByEmailAndSave {}", personalInfo, e);
            throw new ServiceException();
        }
    }
}
