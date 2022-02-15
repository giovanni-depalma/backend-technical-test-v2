package com.tui.proof.domain.entities.base;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@ToString
public class Address {

    @Column(length = 100, nullable = false)
    String street;

    @Column(length = 5, nullable = false)
    String postcode;

    @Column(length = 100, nullable = false)
    String city;

    @Column(length = 100, nullable = false)
    String country;


}
