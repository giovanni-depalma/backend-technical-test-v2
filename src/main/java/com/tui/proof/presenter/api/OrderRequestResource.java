package com.tui.proof.presenter.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record OrderRequestResource(@Schema(example = "10") @Positive int pilotes,
                                   @NotNull @Valid AddressResource delivery,
                                   @NotNull @Valid CustomerResource customer) {
}
