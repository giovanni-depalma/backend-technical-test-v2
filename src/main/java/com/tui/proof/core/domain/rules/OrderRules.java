package com.tui.proof.core.domain.rules;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;


import lombok.AllArgsConstructor;


@AllArgsConstructor
public class OrderRules {
    private final BigDecimal priceForPilote;
    private final Set<Integer> allowedPilotes;
    private final long closeOrderAfterSeconds;
  

    public BigDecimal calculateTotal(int pilotes) {
        return priceForPilote.multiply(BigDecimal.valueOf(pilotes));
    }

    public boolean allowedPilotes(int pilotes){
        return allowedPilotes.contains(pilotes);
    }

    public Instant calculateEditableUntil(Instant now){
        return now.plusSeconds(closeOrderAfterSeconds);
    }


}
