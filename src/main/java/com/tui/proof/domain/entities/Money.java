package com.tui.proof.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Money{
    @Column(name = "money_value", nullable = false)
    BigDecimal value;

    public Money multiply(int n) {
        return new Money(value.multiply(BigDecimal.valueOf(n)));
    }


}
