package com.tui.proof.service.api;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class OrderRequestTest {

    @Test
    public void equalsHashCodeOrder() {
        EqualsVerifier.forClass(OrderRequest.class).verify();
    }
}
