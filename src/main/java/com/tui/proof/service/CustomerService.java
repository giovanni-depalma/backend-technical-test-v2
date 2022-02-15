package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.exception.ServiceException;
import com.tui.proof.mapper.CustomerMapper;
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
    private final CustomerMapper mapper;

    public Optional<Customer> findByEmail(String email){
        try{
            log.debug("findByEmail order: {}", email);
            return customerRepository.findByEmail(email);
        }
        catch(Exception e){
            log.error("error findByEmail: {}", email, e);
            throw new ServiceException();
        }
    }


    public Customer save(Customer customer) {
        try{
            log.debug("save: {}", customer);
            return customerRepository.save(customer);
        }
        catch(Exception e){
            log.error("error save {}", customer, e);
            throw new ServiceException();
        }
    }
}
