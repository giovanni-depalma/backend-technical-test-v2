package com.tui.proof.service.converter;

import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.entities.base.Address;
import com.tui.proof.repository.converter.OrderWriteConverter;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.r2dbc.core.Parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderWriteConverterTest {

    @Test
    public void shouldWrite(){
        OrderWriteConverter converter = new OrderWriteConverter();
        Order order = FakeOrder.buildOrder();
        OutboundRow row = converter.convert(order);
        assertEquals(order.getId(), row.get("id").getValue());
        assertEquals(order.getEditableUntil(), row.get("editable_until").getValue());
        assertEquals(order.getCreatedAt(), row.get("created_at").getValue());
        assertEquals(order.getPilotes(), row.get("pilotes").getValue());
        assertEquals(order.getCustomer().getId(), row.get("customer").getValue());
        assertEquals(order.getTotal().getValue(), row.get("total").getValue());
        Address delivery = order.getDelivery();
        assertEquals(delivery.getCity(), row.get("delivery_city").getValue());
        assertEquals(delivery.getCountry(), row.get("delivery_country").getValue());
        assertEquals(delivery.getPostcode(), row.get("delivery_postcode").getValue());
        assertEquals(delivery.getStreet(), row.get("delivery_street").getValue());
    }
}
