package com.tui.proof.domain.entities;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

}
