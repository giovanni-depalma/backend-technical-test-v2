package com.tui.proof.presenter.rest;

import com.tui.proof.domain.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

@Value
@AllArgsConstructor
public class PurchaserOrder extends RepresentationModel<PurchaserOrder>{
    private Order order;


}
