package com.tui.proof.domain.entities.base;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.tui.proof.domain.entities.base.Address;
import org.junit.jupiter.api.Test;

public class AddressTest {
    @Test
    public void shouldToString() {
        ToStringVerifier.forClass(Address.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .verify();
    }
}
