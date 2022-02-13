package com.tui.proof.service.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.tui.proof.domain.entities.base.Address;
import com.tui.proof.domain.entities.base.PersonalInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderRequest {
    @Schema(example = "10")
    @Positive int pilotes;

    @NotNull
    @Valid Address delivery;

    @NotNull
    @Valid PersonalInfo customer;
}
