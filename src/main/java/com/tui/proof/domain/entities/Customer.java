package com.tui.proof.domain.entities;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "customer_data")
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = {"id"})
public class Customer {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, length = 100, nullable = false)
    String email;

    @Column(nullable = false, length = 100)
    String firstName;

    @Column(nullable = false, length = 100)
    String lastName;

    @Column(nullable = false, length = 30)
    String telephone;

}
