package com.tui.proof.service;

import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.entities.OrderRequest;

import java.util.UUID;

public interface PurchaserOrderService {

    Order createOrder(OrderRequest orderRequest);

    Order updateOrder(UUID uuid, OrderRequest orderRequest);

}
