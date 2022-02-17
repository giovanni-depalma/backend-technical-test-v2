package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.exception.ServiceException;
import com.tui.proof.mapper.CustomerMapper;
import com.tui.proof.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerService {
    private CustomerRepository customerRepository;
    private CustomerMapper mapper;

    public Mono<Customer> findByEmailAndSave(Customer customer) {
        return findByEmail(customer.getEmail()).defaultIfEmpty(new Customer()).map(target -> {
            mapper.update(customer, target);
            return target;
        }).flatMap(this::save);
    }

    public Mono<Customer> findByEmail(String email){
        try{
            log.debug("findByEmail order: {}", email);
            return Mono.justOrEmpty(customerRepository.findByEmail(email));
        }
        catch(Exception e){
            log.error("error findByEmail: {}", email, e);
            return Mono.error(new ServiceException());
        }
    }


    public Mono<Customer> save(Customer customer) {
        try{
            log.debug("save: {}", customer);
            return Mono.just(customerRepository.save(customer));
        }
        catch(Exception e){
            log.error("error save {}", customer, e);
            return Mono.error(new ServiceException());
        }
    }
}
