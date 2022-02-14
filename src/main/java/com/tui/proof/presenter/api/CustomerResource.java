package com.tui.proof.presenter.api;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record CustomerResource(@Schema(example = "EufrasioAlmanzaCepeda@armyspy.com") @Email @Size(min = 1, max = 100) String email,
                               @Schema(example = "Eufrasio Almanza") @NotBlank @Size(min = 1, max = 100) String firstName,
                               @Schema(example = "Cepeda") @NotBlank @Size(min = 1, max = 100) String lastName,
                               @Schema(example = "214-474-0425") @NotBlank @Size(min = 1, max = 30) String telephone) {
}
