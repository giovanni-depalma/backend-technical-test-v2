package com.tui.proof.domain.entities.base;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import com.tui.proof.domain.entities.base.PersonalInfo;
import org.junit.jupiter.api.Test;

public class PersonalInfoTest {

    @Test
    public void shouldToString() {
        ToStringVerifier.forClass(PersonalInfo.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .verify();
    }
}
