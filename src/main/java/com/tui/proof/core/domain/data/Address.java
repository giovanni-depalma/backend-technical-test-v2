package com.tui.proof.core.domain.data;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Address {
  @NotBlank
  private String street;
  @NotBlank
  private String postcode;
  @NotBlank
  private String city;
  @NotBlank
  private String country;
}
