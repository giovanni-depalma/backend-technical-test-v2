package com.tui.proof.domain.entities;

import com.tui.proof.old.CustomerOld;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    public void equalsHashCodeOrder() {
        EqualsVerifier.forClass(CustomerOld.class).verify();
    }

}
