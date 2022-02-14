package com.tui.proof.domain.entities;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerTest {


    @Test
    public void shouldToString() {
        ToStringVerifier.forClass(Customer.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .verify();
    }
}
