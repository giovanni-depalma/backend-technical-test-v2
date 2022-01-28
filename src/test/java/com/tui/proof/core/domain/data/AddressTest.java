package com.tui.proof.core.domain.data;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void equalsHashCodeOrder() {
        EqualsVerifier.forClass(Address.class).verify();
    }
}
