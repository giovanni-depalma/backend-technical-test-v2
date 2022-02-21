package com.tui.proof.repository.converter;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.entities.base.Address;
import com.tui.proof.domain.entities.base.Money;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class OrderReadConverter implements Converter<Row, Order> {
    @Override
    public Order convert(Row source) {
        Address delivery = Address.builder()
                .city(source.get("delivery_city", String.class))
                .country(source.get("delivery_country", String.class))
                .postcode(source.get("delivery_postcode", String.class))
                .street(source.get("delivery_street", String.class))
                .build();
        Order order = new Order();
        order.setId(source.get("id", UUID.class));
        order.setPilotes(source.get("pilotes", Integer.class));
        order.setTotal(new Money(source.get("total", BigDecimal.class)));
        order.setCreatedAt(source.get("created_at", Instant.class));
        order.setEditableUntil(source.get("editable_until", Instant.class));
        order.setDelivery(delivery);
        Customer customer = new Customer();
        customer.setId(source.get("customer", UUID.class));
        order.setCustomer(customer);
        return order;
    }
}
