package com.tui.proof.domain.entities;

import com.tui.proof.domain.entities.base.Address;
import com.tui.proof.domain.entities.base.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_data")
@Getter
@Setter
@ToString
@NamedEntityGraph(name = "graph.order.customer", attributeNodes = @NamedAttributeNode("customer"))
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    @Embedded
    private Money total;

    @Column(nullable = false)
    @Schema(example = "10")
    private Integer pilotes;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    private Customer customer;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "editable_until", nullable = false)
    private Instant editableUntil;

    @Embedded
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
