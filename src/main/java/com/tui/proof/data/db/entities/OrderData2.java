package com.tui.proof.data.db.entities;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;

import com.tui.proof.core.domain.data.Address;
import com.tui.proof.core.domain.data.Address2;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orderdata2")
@Getter
@Setter
public class OrderData2 {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private Integer pilotes;

    /*
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerData customer;
    */

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "editable_until", nullable = false)
    private Instant editableUntil;

    @Embedded
    private Address2 delivery;

}
