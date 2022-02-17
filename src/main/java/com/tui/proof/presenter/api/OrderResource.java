package com.tui.proof.presenter.api;

import com.tui.proof.domain.entities.base.Money;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(name = "Order")
public record OrderResource(UUID id, Money total,
                            Integer pilotes, Instant createdAt, Instant editableUntil,
                            AddressResource delivery,
                            CustomerResource customer) {
}
