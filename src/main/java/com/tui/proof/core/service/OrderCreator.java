package com.tui.proof.core.service;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderRequest;

public interface OrderCreator {
    Order createOrder(OrderRequest orderRequest);
}
