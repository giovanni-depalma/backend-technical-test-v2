package com.tui.proof.service;

import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.exception.BadPilotesOrderException;
import com.tui.proof.domain.exception.EditingClosedOrderException;
import com.tui.proof.domain.exception.ItemNotFoundException;
import com.tui.proof.service.data.OrderRequest;

import java.util.UUID;

public interface PurchaserOrderService {

    Order createOrder(OrderRequest orderRequest) throws BadPilotesOrderException;

    Order updateOrder(UUID uuid, OrderRequest orderRequest) throws EditingClosedOrderException, ItemNotFoundException, BadPilotesOrderException;

}
