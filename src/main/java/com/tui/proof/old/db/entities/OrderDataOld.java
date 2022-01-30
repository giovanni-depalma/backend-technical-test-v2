package com.tui.proof.old.db.entities;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@NamedEntityGraph(name = "graph.orderOld.customer", attributeNodes = @NamedAttributeNode("customer"))
public class OrderDataOld {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private Integer pilotes;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "editable_until", nullable = false)
    private Instant editableUntil;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerDataOld customer;

    @Column(name = "delivery_street", nullable = false)
    private String deliveryStreet;

    @Column(name = "delivery_postcode", nullable = false)
    private String deliveryPostcode;

    @Column(name = "delivery_city", nullable = false)
    private String deliveryCity;

    @Column(name = "delivery_country", nullable = false)
    private String deliveryCountry;
}
