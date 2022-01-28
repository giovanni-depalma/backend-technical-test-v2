package com.tui.proof.core.domain.data;


import lombok.*;

@Value
@Builder
public class Order {
    String id;
    OrderSummary orderSummary;
    Address delivery;
    PersonalInfo customer;

}
