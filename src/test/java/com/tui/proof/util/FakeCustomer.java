package com.tui.proof.util;

import com.github.javafaker.Faker;
import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.base.PersonalInfo;

import java.util.Random;
import java.util.UUID;

public class FakeCustomer {

    public static Customer buildCustomer() {
        Faker faker = new Faker(new Random());
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setPersonalInfo(buildPersonalInfo(faker));
        return customer;
    }

    public static PersonalInfo buildPersonalInfo() {
        Faker faker = new Faker(new Random());
        return buildPersonalInfo(faker);
    }

    private static PersonalInfo buildPersonalInfo(Faker faker) {
        String email = faker.bothify("????##@gmail.com");
        return PersonalInfo.builder().firstName(faker.name().firstName())
                .lastName(faker.name().lastName()).email(email).telephone(faker.phoneNumber().phoneNumber())
                .build();
    }
}
