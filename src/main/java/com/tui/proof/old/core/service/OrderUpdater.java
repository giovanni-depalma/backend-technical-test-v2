package com.tui.proof.old.core.service;

import com.tui.proof.old.OrderOld;
import com.tui.proof.domain.entities.OrderRequest;

public interface OrderUpdater {
    OrderOld updateOrder(String id, OrderRequest orderRequest);
}
