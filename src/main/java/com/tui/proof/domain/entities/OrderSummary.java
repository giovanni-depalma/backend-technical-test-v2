package com.tui.proof.domain.entities;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Embeddable;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Embeddable
public class OrderSummary {
    BigDecimal total;
    int pilotes;
    Instant createdAt;
    Instant editableUntil;
}
