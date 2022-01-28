package com.tui.proof.core.domain.data;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    public void equalsHashCodeOrder() {
        EqualsVerifier.forClass(Order.class).verify();
    }
}
