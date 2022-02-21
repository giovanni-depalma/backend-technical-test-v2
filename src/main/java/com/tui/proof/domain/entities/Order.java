package com.tui.proof.domain.entities;

import com.tui.proof.domain.entities.base.Address;
import com.tui.proof.domain.entities.base.Money;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Table(value = "order_data")
public class Order {
    @Id
    private UUID id;

    @Transient
    private Money total;

    private Integer pilotes;

    @ToString.Exclude
    @Transient
    private Customer customer;

    private Instant createdAt;

    private Instant editableUntil;

    @Transient
    private Address delivery;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(total, order.total) && Objects.equals(pilotes, order.pilotes) && Objects.equals(customer, order.customer) && Objects.equals(createdAt, order.createdAt) && Objects.equals(editableUntil, order.editableUntil) && Objects.equals(delivery, order.delivery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, pilotes, customer, createdAt, editableUntil, delivery);
    }
}
