package com.tui.proof.core.service;

import com.tui.proof.core.domain.data.Order;
import com.tui.proof.core.domain.data.OrderRequest;

import java.util.Optional;

public interface OrderUpdater {
    Order updateOrder(String id, OrderRequest orderRequest);
}
