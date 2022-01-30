package com.tui.proof.domain.entities;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerTest {

    @Test
    public void shouldGetAndSet() {
        Customer customer = new Customer();
        PersonalInfo personalInfo = new PersonalInfo();
        UUID id = UUID.randomUUID();
        customer.setId(id);
        customer.setPersonalInfo(personalInfo);
        assertEquals(id, customer.getId());
        assertEquals(personalInfo, customer.getPersonalInfo());
    }

    @Test
    public void shouldToString() {
        ToStringVerifier.forClass(Customer.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .verify();
    }
}
