package com.tui.proof.util;

import com.github.javafaker.Faker;
import com.tui.proof.domain.entities.Customer;
import com.tui.proof.presenter.api.CustomerResource;

import java.util.Random;
import java.util.UUID;

public class FakeCustomer {

    public static Customer buildCustomer() {
        Faker faker = new Faker(new Random());
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        String email = faker.bothify("????##@gmail.com");
        customer.setEmail(email);
        customer.setFirstName(faker.name().firstName());
        customer.setLastName(faker.name().lastName());
        customer.setTelephone(faker.phoneNumber().phoneNumber());
        return customer;
    }


    public static  CustomerResource buildResource(Customer item){
        return new CustomerResource(item.getEmail(), item.getFirstName(), item.getLastName(), item.getTelephone());
    }

}
