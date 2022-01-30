package com.tui.proof.domain.entities;

import com.tui.proof.old.OrderOld;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class OrderTest {

    @Test
    public void equalsHashCodeOrder() {
        EqualsVerifier.forClass(OrderOld.class).verify();
    }
}
