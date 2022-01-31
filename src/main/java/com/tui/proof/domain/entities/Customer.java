package com.tui.proof.domain.entities;

import com.tui.proof.domain.entities.base.PersonalInfo;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "customer_data")
@Getter
@Setter
@ToString
public class Customer {
    @Id
    @GeneratedValue
    private UUID id;

    @Embedded
    private PersonalInfo personalInfo;

}
