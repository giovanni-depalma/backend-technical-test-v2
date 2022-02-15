package com.tui.proof.presenter.api;

import com.tui.proof.domain.entities.base.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.server.core.Relation;

import java.time.Instant;
import java.util.UUID;

@Schema(name = "Order")
@Relation(collectionRelation = "orders")
public record OrderResource(UUID id, Money total,
                            Integer pilotes, Instant createdAt, Instant editableUntil,
                            AddressResource delivery,
                            CustomerResource customer) {
}
