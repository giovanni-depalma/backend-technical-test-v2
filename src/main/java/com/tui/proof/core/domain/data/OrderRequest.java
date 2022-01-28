package com.tui.proof.core.domain.data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderRequest {
    @Positive
    private int pilotes;

    @NotNull
    @Valid
    private Address delivery;

    @NotNull
    @Valid
    private PersonalInfo customer;
}
