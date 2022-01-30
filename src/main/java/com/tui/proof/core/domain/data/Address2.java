package com.tui.proof.core.domain.data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
public class Address2 {
  @NotBlank String street;

  @NotBlank String postcode;

  @NotBlank String city;

  @NotBlank String country;
}
