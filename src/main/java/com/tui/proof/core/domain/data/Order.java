package com.tui.proof.core.domain.data;


import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
public class Order {
    @With
    private final Status status;
    private final OrderSummary orderSummary;
    private final Address delivery;
    private final PersonalInfo customer;

}
