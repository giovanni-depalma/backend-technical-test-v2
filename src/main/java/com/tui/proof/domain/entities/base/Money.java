package com.tui.proof.domain.entities.base;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@ToString
public class Money{
    @Column(name = "money_value", nullable = false)
    BigDecimal value;

    public Money(String value){
        this(new BigDecimal(value));
    }

    public Money multiply(int n) {
        return new Money(value.multiply(BigDecimal.valueOf(n)));
    }


}
