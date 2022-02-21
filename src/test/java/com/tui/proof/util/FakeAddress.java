package com.tui.proof.util;

import com.github.javafaker.Faker;
import com.tui.proof.domain.entities.base.Address;
import com.tui.proof.presenter.api.AddressResource;

import java.util.Random;

public class FakeAddress {

    public static Address buildAddress() {
        Faker faker = new Faker(new Random());
        return Address.builder().city(faker.address().city()).country(faker.address().country())
                .postcode(faker.number().digits(5)).street(faker.address().streetAddress()).build();
    }

    public static AddressResource buildResource(Address address){
        return new AddressResource(address.getStreet(), address.getPostcode(), address.getCity(), address.getCountry());
    }
}
