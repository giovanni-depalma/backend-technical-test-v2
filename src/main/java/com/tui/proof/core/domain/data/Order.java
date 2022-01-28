package com.tui.proof.core.domain.data;


import lombok.*;

@Value
@Builder
public class Order {
    private final String id;
    private final OrderSummary orderSummary;
    private final Address delivery;
    private final PersonalInfo customer;

}
