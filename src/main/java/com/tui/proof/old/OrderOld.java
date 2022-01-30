package com.tui.proof.old;


import com.tui.proof.domain.entities.Address;
import com.tui.proof.domain.entities.OrderSummary;
import com.tui.proof.domain.entities.PersonalInfo;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderOld {
    String id;
    OrderSummary orderSummary;
    Address delivery;
    PersonalInfo customer;

}
