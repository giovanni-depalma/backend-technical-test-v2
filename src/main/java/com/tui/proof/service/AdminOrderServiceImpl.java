package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.repositories.Order2DataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService{
    private final Order2DataRepository orderRepository;

    @Override
    public List<Order> findByCustomer(PersonalInfo personalInfo) {
        log.debug("findByCustomer with by example {}", personalInfo);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase();
        Order orderData = new Order();
        Customer customer = new Customer();
        customer.setPersonalInfo(personalInfo);
        orderData.setCustomer(customer);
        return orderRepository.findAll(Example.of(orderData, matcher));
    }
}
