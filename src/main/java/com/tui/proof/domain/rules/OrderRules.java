package com.tui.proof.domain.rules;

import java.time.Instant;
import java.util.Set;


import com.tui.proof.domain.entities.base.Money;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class OrderRules {
    private final Money priceForPilote;
    private final Set<Integer> allowedPilotes;
    private final long closeOrderAfterSeconds;

    public Money calculateTotal(int pilotes) {
        return priceForPilote.multiply(pilotes);
    }

    public boolean allowedPilotes(int pilotes){
        return allowedPilotes.contains(pilotes);
    }

    public Instant calculateEditableUntil(Instant now){
        return now.plusSeconds(closeOrderAfterSeconds);
    }


}
