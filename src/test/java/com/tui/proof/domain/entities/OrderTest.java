package com.tui.proof.domain.entities;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class OrderTest {
    @Test
    public void shouldToString() {
        ToStringVerifier.forClass(Order.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .withIgnoredFields("customer")
                .withFailOnExcludedFields(true)
                .verify();
    }
}
