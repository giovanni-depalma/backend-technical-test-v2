package com.tui.proof.presenter.api;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Schema(name = "Address")
public record AddressResource(
        @Schema(example = "Poco Mas Drive") @Size(min = 1, max = 100) @NotBlank String street,
        @Schema(example = "24205") @Pattern(regexp = "^\\d{5}$") @NotBlank String postcode,
        @Schema(example = "Frisco") @Size(min = 1, max = 100) @NotBlank String city,
        @Schema(example = "TX") @Size(min = 1, max = 100) @NotBlank String country) {
}
