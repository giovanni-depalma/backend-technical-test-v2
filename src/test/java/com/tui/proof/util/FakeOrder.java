package com.tui.proof.util;

import com.github.javafaker.Faker;
import com.tui.proof.domain.entities.*;
import com.tui.proof.domain.entities.base.Money;
import com.tui.proof.presenter.api.AddressResource;
import com.tui.proof.presenter.api.CustomerResource;
import com.tui.proof.presenter.api.OrderRequestResource;
import com.tui.proof.service.api.OrderRequest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;
import java.util.UUID;
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

    private static Money fakeTotal(Faker faker) {
        return new Money(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100)));
    }

    public static Order buildOrder() {
        Faker faker = new Faker(new Random());
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setCreatedAt(fakeCreatedAt(faker));
        order.setDelivery(FakeAddress.buildAddress());
        order.setPilotes(fakePilotes(faker));
        order.setTotal(fakeTotal(faker));
        order.setEditableUntil(fakeEditableUntil(faker));
        order.setCustomer(FakeCustomer.buildCustomer());
        return order;
    }

    public static Order buildOrder(OrderRequest request) {
        Order order = buildOrder();
        order.setCustomer(request.customer());
        order.setDelivery(request.delivery());
        order.setPilotes(request.pilotes());
        return order;
    }

    public static Order buildOrderWithId(UUID id, Instant createdAt, Instant editableUntil, OrderRequest request) {
        Order order = buildOrder(request);
        order.setCreatedAt(createdAt);
        order.setEditableUntil(editableUntil);
        order.setId(id);
        return order;
    }

    public static OrderRequest buildOrderRequest() {
        Faker faker = new Faker(new Random());
        return new OrderRequest(fakePilotes(faker),FakeAddress.buildAddress(),FakeCustomer.buildCustomer());
    }

    public static OrderRequest buildBadOrderRequest() {
        return new OrderRequest(0, FakeAddress.buildAddress(),FakeCustomer.buildCustomer());
    }

    public static  OrderRequestResource buildResource(OrderRequest item){
        AddressResource delivery = FakeAddress.buildResource(item.delivery());
        CustomerResource customer = FakeCustomer.buildResource(item.customer());
        return new OrderRequestResource(item.pilotes(), delivery, customer);
    }
}
