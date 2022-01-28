package com.tui.proof.util;

import com.github.javafaker.Faker;
import com.tui.proof.core.domain.data.Address;
import com.tui.proof.core.domain.data.Order;

import java.util.Random;

public class FakeAddress {

    public static Address buildAddress() {
        Faker faker = new Faker(new Random());
        return Address.builder().city(faker.address().city()).country(faker.address().country())
                .postcode(faker.address().zipCode()).street(faker.address().streetAddress()).build();
    }
}
