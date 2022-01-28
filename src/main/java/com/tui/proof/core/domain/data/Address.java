package com.tui.proof.core.domain.data;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Address {
  @NotBlank String street;

  @NotBlank String postcode;

  @NotBlank String city;

  @NotBlank String country;
}
