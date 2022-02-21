package com.tui.proof.domain.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Table(value = "customer_data")
public class Customer {
    @Id
    private UUID id;

    String email;

    String firstName;

    String lastName;

    String telephone;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(email, customer.email) && Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName) && Objects.equals(telephone, customer.telephone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastName, telephone);
    }
}
