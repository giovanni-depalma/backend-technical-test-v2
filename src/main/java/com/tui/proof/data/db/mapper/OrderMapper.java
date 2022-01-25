package com.tui.proof.data.db.mapper;


import com.tui.proof.core.domain.data.Address;
import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderSummary;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.data.db.entities.CustomerData;
import com.tui.proof.data.db.entities.OrderData;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderMapper {

    private final CustomerMapper customerMapper;

    public Order toDomain(OrderData orderData) {
        OrderSummary orderSummary = OrderSummary.builder().id(String.valueOf(orderData.getId()))
                .total(orderData.getTotal())
                .pilotes(orderData.getPilotes())
                .createdAt(orderData.getCreatedAt())
                .updatedAt(orderData.getUpdatedAt())
                .build();
        Address delivery = Address.builder().street(orderData.getDeliveryStreet())
                .postcode(orderData.getDeliveryPostcode())
                .city(orderData.getDeliveryCity()).country(orderData.getDeliveryCountry()).build();
        PersonalInfo customer = customerMapper.toPersonalInfo(orderData.getCustomer());
        return Order.builder().customer(customer).delivery(delivery).orderSummary(orderSummary).build();
    }

    public void populateData(OrderData orderData, Order order, CustomerData customer) {
        OrderSummary summary = order.getOrderSummary();
        orderData.setTotal(summary.getTotal());
        orderData.setPilotes(summary.getPilotes());
        orderData.setCreatedAt(summary.getCreatedAt());
        orderData.setUpdatedAt(summary.getUpdatedAt());
        Address delivery = order.getDelivery();
        orderData.setDeliveryStreet(delivery.getStreet());
        orderData.setDeliveryPostcode(delivery.getPostcode());
        orderData.setDeliveryCity(delivery.getCity());
        orderData.setDeliveryCountry(delivery.getCountry());
        orderData.setCustomer(customer);
    }

}
