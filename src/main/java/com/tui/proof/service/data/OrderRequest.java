package com.tui.proof.service.data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.tui.proof.domain.entities.base.Address;
import com.tui.proof.domain.entities.base.PersonalInfo;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
public class OrderRequest {
    @Positive int pilotes;

    @NotNull
    @Valid Address delivery;

    @NotNull
    @Valid PersonalInfo customer;
}
