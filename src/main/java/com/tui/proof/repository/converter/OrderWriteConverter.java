package com.tui.proof.repository.converter;

import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.entities.base.Address;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.r2dbc.core.Parameter;

import java.util.UUID;

public class OrderWriteConverter implements Converter<Order, OutboundRow> {
    @Override
    public OutboundRow convert(Order source) {
        OutboundRow row = new OutboundRow();
        row.put("id", Parameter.fromOrEmpty(source.getId(), UUID.class));
        row.put("editable_until", Parameter.from(source.getEditableUntil()));
        row.put("created_at", Parameter.from(source.getCreatedAt()));
        row.put("pilotes", Parameter.from(source.getPilotes()));
        row.put("customer", Parameter.from(source.getCustomer().getId()));
        row.put("total", Parameter.from(source.getTotal().getValue()));
        Address delivery = source.getDelivery();
        row.put("delivery_city", Parameter.from(delivery.getCity()));
        row.put("delivery_country", Parameter.from(delivery.getCountry()));
        row.put("delivery_postcode", Parameter.from(delivery.getPostcode()));
        row.put("delivery_street", Parameter.from(delivery.getStreet()));
        return row;
    }
}
