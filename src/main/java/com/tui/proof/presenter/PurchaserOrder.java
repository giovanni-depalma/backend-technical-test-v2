package com.tui.proof.presenter;

import com.tui.proof.domain.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

@Value
@AllArgsConstructor
public class PurchaserOrder{
    Order order;


}
