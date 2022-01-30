package com.tui.proof.domain.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    @NotBlank String street;

    @Size(min = 1, max = 10)
    @Column(length = 10, nullable = false)
    @NotBlank String postcode;

    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    @NotBlank String city;

    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    @NotBlank String country;


}
