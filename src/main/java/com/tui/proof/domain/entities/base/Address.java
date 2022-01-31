package com.tui.proof.domain.entities.base;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@ToString
public class Address {
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    @NotBlank String street;

    @Pattern(regexp = "^\\d{5}$")
    @Column(length = 5, nullable = false)
    @NotBlank String postcode;

    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    @NotBlank String city;

    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    @NotBlank String country;


}
