package com.tui.proof.data.db.mapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;

import com.github.javafaker.Faker;
import com.tui.proof.core.domain.data.Address;
import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderSummary;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.data.db.entities.CustomerData;
import com.tui.proof.data.db.entities.OrderData;
import com.tui.proof.data.db.mapper.CustomerMapper;
import com.tui.proof.data.db.mapper.OrderMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class OrderMapperTest {

    @Test
    public void shouldMapToDomain() {
        CustomerMapper customerMapper = Mockito.mock(CustomerMapper.class);
        OrderMapper mapper = new OrderMapper(customerMapper);
        Faker faker = new Faker(new Random());
        OrderData orderData = new OrderData();
        orderData.setCreatedAt(Instant.now());
        orderData.setDeliveryCity(faker.address().city());
        orderData.setDeliveryCountry(faker.address().country());
        orderData.setDeliveryPostcode(faker.address().zipCode());
        orderData.setDeliveryStreet(faker.address().streetName());
        orderData.setId(faker.number().randomNumber());
        orderData.setPilotes(faker.number().numberBetween(1, 100));
        orderData.setTotal(new BigDecimal(faker.number().randomDouble(2, 1, 100)));
        orderData.setUpdatedAt(Instant.now());
        CustomerData customerData = new CustomerData();
        orderData.setCustomer(customerData);
        PersonalInfo expectedPersonalInfo = Mockito.mock(PersonalInfo.class);
        when(customerMapper.toPersonalInfo(customerData)).thenReturn(expectedPersonalInfo);
        Order actual = mapper.toDomain(orderData);
        assertAll(
                () -> assertEquals(orderData.getCreatedAt(), actual.getOrderSummary().getCreatedAt()),
                () -> assertEquals(orderData.getDeliveryCity(), actual.getDelivery().getCity()),
                () -> assertEquals(orderData.getDeliveryCountry(), actual.getDelivery().getCountry()),
                () -> assertEquals(orderData.getDeliveryPostcode(), actual.getDelivery().getPostcode()),
                () -> assertEquals(orderData.getDeliveryStreet(), actual.getDelivery().getStreet()),
                () -> assertEquals(orderData.getId(), Long.parseLong(actual.getOrderSummary().getId())),
                () -> assertEquals(orderData.getPilotes(), actual.getOrderSummary().getPilotes()),
                () -> assertEquals(orderData.getTotal(), actual.getOrderSummary().getTotal()),
                () -> assertEquals(orderData.getUpdatedAt(), actual.getOrderSummary().getUpdatedAt()),
                () -> assertEquals(expectedPersonalInfo, actual.getCustomer()));
    }

    @Test
    public void shouldPopulateData() {
        CustomerMapper customerMapper = Mockito.mock(CustomerMapper.class);
        OrderMapper mapper = new OrderMapper(customerMapper);
        OrderData orderData = new OrderData();
        CustomerData customerData = Mockito.mock(CustomerData.class);
        Faker faker = new Faker(new Random());

        orderData.setUpdatedAt(Instant.now());
        Address delivery = Address.builder().city(faker.address().city()).country(faker.address().country()).postcode(
                faker.address().zipCode()).street(faker.address().streetName()).build();
        OrderSummary orderSummary = OrderSummary.builder().createdAt(Instant.now())
                .pilotes(faker.number().numberBetween(1, 100))
                .total(new BigDecimal(faker.number().randomDouble(2, 1, 100))).updatedAt(Instant.now()).build();
        Order order = Order.builder().delivery(delivery).orderSummary(orderSummary).build();
        mapper.populateData(orderData, order, customerData);
        assertAll(
                () -> assertEquals(order.getOrderSummary().getCreatedAt(), orderData.getCreatedAt()),
                () -> assertEquals(customerData, orderData.getCustomer()),
                () -> assertEquals(order.getDelivery().getCity(), orderData.getDeliveryCity()),
                () -> assertEquals(order.getDelivery().getCountry(), orderData.getDeliveryCountry()),
                () -> assertEquals(order.getDelivery().getPostcode(), orderData.getDeliveryPostcode()),
                () -> assertEquals(order.getDelivery().getStreet(), orderData.getDeliveryStreet()),
                () -> assertEquals(order.getOrderSummary().getPilotes(), orderData.getPilotes()),
                () -> assertEquals(order.getOrderSummary().getTotal(), orderData.getTotal()),
                () -> assertEquals(order.getOrderSummary().getUpdatedAt(), orderData.getUpdatedAt()));
    }

}
