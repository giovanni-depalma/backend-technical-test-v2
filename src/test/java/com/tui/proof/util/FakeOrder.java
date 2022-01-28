package com.tui.proof.util;

import com.github.javafaker.Faker;
import com.tui.proof.core.domain.data.*;
import com.tui.proof.data.db.entities.OrderData;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FakeOrder {

    public static Instant fakeCreatedAt(Faker faker) {
        return faker.date().future(10, TimeUnit.SECONDS).toInstant();
    }

    public static Instant fakeEditableUntil(Faker faker) {
        return faker.date().future(60 * 5, TimeUnit.SECONDS).toInstant();
    }

    private static int fakePilotes(Faker faker) {
        return faker.number().numberBetween(1, 100);
    }

    private static BigDecimal fakeTotal(Faker faker) {
        return BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100));
    }


    public static OrderData buildOrderData() {
        Faker faker = new Faker(new Random());
        OrderData orderData = new OrderData();
        orderData.setCreatedAt(fakeCreatedAt(faker));
        orderData.setDeliveryCity(faker.address().city());
        orderData.setDeliveryCountry(faker.address().country());
        orderData.setDeliveryPostcode(faker.address().zipCode());
        orderData.setDeliveryStreet(faker.address().streetName());
        orderData.setId(faker.number().randomNumber());
        orderData.setPilotes(fakePilotes(faker));
        orderData.setTotal(fakeTotal(faker));
        orderData.setEditableUntil(fakeEditableUntil(faker));
        orderData.setId(faker.number().randomNumber());
        orderData.setCustomer(FakeCustomer.buildCustomerData());
        return orderData;
    }

    public static Order buildOrder() {
        Faker faker = new Faker(new Random());
        PersonalInfo customer = FakeCustomer.buildPersonalInfo();
        Instant createdAt = fakeCreatedAt(faker);
        Instant editableUntil = fakeEditableUntil(faker);
        BigDecimal total = BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100));
        OrderSummary orderSummary = OrderSummary.builder().editableUntil(editableUntil).createdAt(createdAt)
                .total(total).pilotes(fakePilotes(faker)).build();
        String id = String.valueOf(faker.number().randomNumber());
        return Order.builder().id(id).customer(customer).orderSummary(orderSummary).delivery(FakeAddress.buildAddress()).build();
    }

    public static Order buildOrder(OrderRequest request) {
        Faker faker = new Faker(new Random());
        Instant createdAt = fakeCreatedAt(faker);
        Instant editableUntil = fakeEditableUntil(faker);
        BigDecimal total = BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100));
        OrderSummary orderSummary = OrderSummary.builder().editableUntil(editableUntil).createdAt(createdAt)
                .total(total).pilotes(request.getPilotes()).build();
        String id = String.valueOf(faker.number().randomNumber());
        return Order.builder().id(id).customer(request.getCustomer()).orderSummary(orderSummary).delivery(request.getDelivery()).build();
    }

    public static Order buildOrderWithId(String id, Instant createdAt, Instant editableUntil, OrderRequest request) {
        Faker faker = new Faker(new Random());
        BigDecimal total = BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100));
        OrderSummary orderSummary = OrderSummary.builder().editableUntil(editableUntil).createdAt(createdAt)
                .total(total).pilotes(request.getPilotes()).build();
        return Order.builder().id(id).customer(request.getCustomer()).orderSummary(orderSummary).delivery(request.getDelivery()).build();
    }

    public static OrderRequest buildOrderRequest() {
        Faker faker = new Faker(new Random());
        PersonalInfo customer = FakeCustomer.buildPersonalInfo();
        return OrderRequest.builder().pilotes(fakePilotes(faker)).customer(customer).delivery(FakeAddress.buildAddress()).build();
    }

    public static OrderRequest buildBadOrderRequest() {
        PersonalInfo customer = FakeCustomer.buildPersonalInfo();
        return OrderRequest.builder().customer(customer).delivery(FakeAddress.buildAddress()).build();
    }
}
