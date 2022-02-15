package com.tui.proof.domain.entities;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.*;

import com.tui.proof.domain.entities.base.Address;
import com.tui.proof.domain.entities.base.Money;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "order_data")
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"id"})
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

}
