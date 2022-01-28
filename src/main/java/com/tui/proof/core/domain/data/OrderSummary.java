package com.tui.proof.core.domain.data;

import java.math.BigDecimal;
import java.time.Instant;


import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
public class OrderSummary {
    private final BigDecimal total;
    private final int pilotes;
    private final Instant createdAt;
    private final Instant editableUntil;
}
