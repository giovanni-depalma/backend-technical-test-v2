package com.tui.proof.domain.entities.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Money{
    BigDecimal value;

    public Money(String value){
        this(new BigDecimal(value));
    }

    public Money multiply(int n) {
        return new Money(value.multiply(BigDecimal.valueOf(n)));
    }


}
