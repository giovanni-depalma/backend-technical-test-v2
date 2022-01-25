package com.tui.proof.core.domain.data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Value;

@Value
public class OrderRequest {
    @Positive
    private int pilotes;
    @NotNull
    private Address delivery;
    @NotNull
    private PersonalInfo customer;
}
