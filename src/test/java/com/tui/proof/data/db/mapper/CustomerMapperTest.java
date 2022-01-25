package com.tui.proof.data.db.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import com.github.javafaker.Faker;
import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.data.db.entities.CustomerData;
import com.tui.proof.data.db.mapper.CustomerMapper;

import org.junit.jupiter.api.Test;

public class CustomerMapperTest {

    private CustomerData createCustomerData() {
        CustomerData customerData = new CustomerData();
        Faker faker = new Faker(new Random());
        customerData.setFirstName(faker.name().firstName());
        customerData.setLastName(faker.name().firstName());
        String email = faker.bothify("????##@gmail.com");
        customerData.setEmail(email);
        customerData.setTelephone(faker.phoneNumber().phoneNumber());
        customerData.setId(1l);
        return customerData;
    }

    @Test
    public void shouldMapToPersonalInfo() {
        CustomerMapper mapper = new CustomerMapper();
        CustomerData customerData = createCustomerData();
        PersonalInfo actual = mapper.toPersonalInfo(customerData);
        assertAll(
                () -> assertEquals(customerData.getFirstName(), actual.getFirstName()),
                () -> assertEquals(customerData.getLastName(), actual.getLastName()),
                () -> assertEquals(customerData.getEmail(), actual.getEmail()),
                () -> assertEquals(customerData.getTelephone(), actual.getTelephone()));
    }

    @Test
    public void shouldMapToDomain() {
        CustomerMapper mapper = new CustomerMapper();
        CustomerData customerData = createCustomerData();
        Customer actual = mapper.toDomain(customerData);
        PersonalInfo actualPersonalInfo = actual.getPersonalInfo();
        assertAll(
                () -> assertEquals(customerData.getId(), actual.getId()),
                () -> assertEquals(customerData.getFirstName(), actualPersonalInfo.getFirstName()),
                () -> assertEquals(customerData.getLastName(), actualPersonalInfo.getLastName()),
                () -> assertEquals(customerData.getEmail(), actualPersonalInfo.getEmail()),
                () -> assertEquals(customerData.getTelephone(), actualPersonalInfo.getTelephone()));
    }

    @Test
    public void shouldPopulateData() {
        CustomerMapper mapper = new CustomerMapper();
        CustomerData customerData = new CustomerData();
        Faker faker = new Faker(new Random());
        String email = faker.bothify("????##@gmail.com");
        PersonalInfo personalInfo = PersonalInfo.builder().firstName(faker.name().firstName())
                .lastName(faker.name().lastName()).email(email).telephone(faker.phoneNumber().phoneNumber())
                .build();

        mapper.populateData(customerData, personalInfo);
        assertAll(
                () -> assertEquals(personalInfo.getFirstName(), customerData.getFirstName()),
                () -> assertEquals(personalInfo.getLastName(), customerData.getLastName()),
                () -> assertEquals(personalInfo.getEmail(), customerData.getEmail()),
                () -> assertEquals(personalInfo.getTelephone(), customerData.getTelephone()));
    }

}
