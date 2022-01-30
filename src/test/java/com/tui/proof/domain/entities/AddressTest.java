package com.tui.proof.domain.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void equalsHashCodeOrder() {
        EqualsVerifier.forClass(Address.class).verify();
    }
}
