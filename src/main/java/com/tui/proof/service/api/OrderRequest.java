package com.tui.proof.service.api;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.base.Address;

public record OrderRequest(int pilotes, Address delivery,
                           Customer customer) {
}
