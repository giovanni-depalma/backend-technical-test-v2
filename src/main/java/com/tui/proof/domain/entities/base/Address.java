package com.tui.proof.domain.entities.base;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@ToString
public class Address {

    @Schema(example = "Poco Mas Drive")
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    @NotBlank String street;

    @Schema(example = "24205")
    @Pattern(regexp = "^\\d{5}$")
    @Column(length = 5, nullable = false)
    @NotBlank String postcode;

    @Schema(example = "Frisco")
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    @NotBlank String city;

    @Schema(example = "TX")
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    @NotBlank String country;


}
