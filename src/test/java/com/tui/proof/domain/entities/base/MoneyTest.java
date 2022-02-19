package com.tui.proof.domain.entities.base;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.tui.proof.domain.entities.base.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTest {

    @Test
    public void shouldToString() {
        ToStringVerifier.forClass(Money.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .verify();
    }

    @Test
    public void shouldConstructByString() {
        String value = "2.20";
        Money expected = new Money(new BigDecimal(value));
        Money actual = new Money(value);
        assertEquals(expected,actual);
    }
}
