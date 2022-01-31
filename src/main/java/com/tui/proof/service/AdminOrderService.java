package com.tui.proof.service;

import com.tui.proof.domain.entities.Order;
import com.tui.proof.domain.entities.base.PersonalInfo;

import java.util.List;

public interface AdminOrderService {
    List<Order> findByCustomer(PersonalInfo customer);
}
