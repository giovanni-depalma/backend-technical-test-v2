package com.tui.proof.presenter.data;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.Order;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PurchaserOrderMapper implements Function<Order, PurchaserOrder> {
    @Override
    public PurchaserOrder apply(Order order) {
        Customer customer = order.getCustomer();
        return PurchaserOrder.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .pilotes(order.getPilotes())
                .total(order.getTotal())
                .delivery(order.getDelivery())
                .editableUntil(order.getEditableUntil())
                .customer(customer == null ? null : customer.getPersonalInfo())
                .build();
    }
}
