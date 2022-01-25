package com.tui.proof.core.domain.data;

import java.math.BigDecimal;
import java.time.Instant;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderSummary {
    private final String id;
    private final BigDecimal total;
    private final int pilotes;
    private final Instant createdAt;
    private final Instant updatedAt;
}
