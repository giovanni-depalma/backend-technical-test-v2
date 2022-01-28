package com.tui.proof.util;

import com.github.javafaker.Commerce;
import com.github.javafaker.Faker;
import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.data.db.entities.CustomerData;

import java.util.Random;

public class FakeCustomer {

    public static Customer buildCustomer() {
        Faker faker = new Faker(new Random());
        return Customer.builder().personalInfo(buildPersonalInfo(faker)).id(faker.number().numberBetween(1,100)).build();
    }

    public static PersonalInfo buildPersonalInfo() {
        Faker faker = new Faker(new Random());
        return buildPersonalInfo(faker);
    }

    public static CustomerData buildCustomerData(){
        Faker faker = new Faker(new Random());
        String email = faker.bothify("????##@gmail.com");
        CustomerData customerData = new CustomerData();
        customerData.setEmail(email);
        customerData.setFirstName(faker.name().firstName());
        customerData.setLastName(faker.name().lastName());
        customerData.setTelephone(faker.phoneNumber().phoneNumber());
        customerData.setId(faker.number().randomNumber());
        return customerData;
    }

    private static PersonalInfo buildPersonalInfo(Faker faker) {
        String email = faker.bothify("????##@gmail.com");
        return PersonalInfo.builder().firstName(faker.name().firstName())
                .lastName(faker.name().lastName()).email(email).telephone(faker.phoneNumber().phoneNumber())
                .build();
    }
}
