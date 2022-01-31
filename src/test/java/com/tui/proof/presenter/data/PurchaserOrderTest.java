package com.tui.proof.presenter.data;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class PurchaserOrderTest {

    @Test
    public void shouldEqualsAndHashcode() {
        EqualsVerifier.forClass(PurchaserOrder.class)
                .verify();
    }

    @Test
    public void shouldToString() {
        ToStringVerifier.forClass(PurchaserOrder.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .verify();
    }
}
