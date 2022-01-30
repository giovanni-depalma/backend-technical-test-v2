package com.tui.proof.util;

import com.github.javafaker.Faker;
import com.tui.proof.old.CustomerOld;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.old.db.entities.CustomerDataOld;

import java.util.Random;

public class FakeCustomer {

    public static CustomerOld buildCustomer() {
        Faker faker = new Faker(new Random());
        return CustomerOld.builder().personalInfo(buildPersonalInfo(faker)).id(faker.number().numberBetween(1,100)).build();
    }

    public static PersonalInfo buildPersonalInfo() {
        Faker faker = new Faker(new Random());
        return buildPersonalInfo(faker);
    }

    public static CustomerDataOld buildCustomerData(){
        Faker faker = new Faker(new Random());
        String email = faker.bothify("????##@gmail.com");
        CustomerDataOld customerData = new CustomerDataOld();
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
