package com.tui.proof.old.db.mapper;


import com.tui.proof.domain.entities.Address;
import com.tui.proof.old.OrderOld;
import com.tui.proof.domain.entities.OrderSummary;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.old.db.entities.CustomerDataOld;
import com.tui.proof.old.db.entities.OrderDataOld;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderMapper {

    private final CustomerMapper customerMapper;

    public OrderOld toDomain(OrderDataOld orderData) {
        OrderSummary orderSummary = OrderSummary.builder()
                .total(orderData.getTotal())
                .pilotes(orderData.getPilotes())
                .createdAt(orderData.getCreatedAt())
                .editableUntil(orderData.getEditableUntil())
                .build();
        Address delivery = Address.builder().street(orderData.getDeliveryStreet())
                .postcode(orderData.getDeliveryPostcode())
                .city(orderData.getDeliveryCity()).country(orderData.getDeliveryCountry()).build();
        PersonalInfo customer = customerMapper.toPersonalInfo(orderData.getCustomer());
        return OrderOld.builder().id(String.valueOf(orderData.getId())).customer(customer).delivery(delivery).orderSummary(orderSummary).build();
    }

    public void populateData(OrderDataOld orderData, OrderOld order, CustomerDataOld customer) {
        OrderSummary summary = order.getOrderSummary();
        orderData.setTotal(summary.getTotal());
        orderData.setPilotes(summary.getPilotes());
        orderData.setCreatedAt(summary.getCreatedAt());
        orderData.setEditableUntil(summary.getEditableUntil());
        Address delivery = order.getDelivery();
        orderData.setDeliveryStreet(delivery.getStreet());
        orderData.setDeliveryPostcode(delivery.getPostcode());
        orderData.setDeliveryCity(delivery.getCity());
        orderData.setDeliveryCountry(delivery.getCountry());
        orderData.setCustomer(customer);
    }

}
