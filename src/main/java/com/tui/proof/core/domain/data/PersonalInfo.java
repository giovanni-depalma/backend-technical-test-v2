package com.tui.proof.core.domain.data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PersonalInfo {
    @Email String email;
    @NotBlank String firstName;
    @NotBlank String lastName;
    @NotBlank String telephone;
}
