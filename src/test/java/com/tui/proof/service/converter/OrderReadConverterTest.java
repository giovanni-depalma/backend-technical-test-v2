package com.tui.proof.service.converter;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.entities.base.Address;
import com.tui.proof.repository.converter.OrderReadConverter;
import com.tui.proof.repository.converter.OrderWriteConverter;
import com.tui.proof.util.FakeOrder;
import io.r2dbc.spi.Row;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.r2dbc.mapping.OutboundRow;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderReadConverterTest {

    @Test
    public void shouldRead(){
        OrderReadConverter converter = new OrderReadConverter();
        Order expected = FakeOrder.buildOrder();
        Customer customerWithOnlyId = new Customer();
        customerWithOnlyId.setId(expected.getId());
        expected.setCustomer(customerWithOnlyId);
        Address delivery = expected.getDelivery();
        Row row = Mockito.mock(Row.class);
        Mockito.when(row.get("delivery_city", String.class)).thenReturn(delivery.getCity());
        Mockito.when(row.get("delivery_street", String.class)).thenReturn(delivery.getStreet());
        Mockito.when(row.get("delivery_postcode", String.class)).thenReturn(delivery.getPostcode());
        Mockito.when(row.get("delivery_country", String.class)).thenReturn(delivery.getCountry());
        Mockito.when(row.get("id", UUID.class)).thenReturn(expected.getId());
        Mockito.when(row.get("created_at", Instant.class)).thenReturn(expected.getCreatedAt());
        Mockito.when(row.get("editable_until", Instant.class)).thenReturn(expected.getEditableUntil());
        Mockito.when(row.get("pilotes", Integer.class)).thenReturn(expected.getPilotes());
        Mockito.when(row.get("total", BigDecimal.class)).thenReturn(expected.getTotal().getValue()  );
        Mockito.when(row.get("customer", UUID.class)).thenReturn(expected.getCustomer().getId());
        Order actual = converter.convert(row);
        assertEquals(expected, actual);

    }
}
