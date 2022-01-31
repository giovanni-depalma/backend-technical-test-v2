package com.tui.proof.domain.entities.base;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonalInfo {
    @Email
    @Size(min = 1, max = 100)
    @Column(unique = true, length = 100, nullable = false)
    String email;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    String firstName;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    String lastName;

    @NotBlank
    @Size(min = 1, max = 30)
    @Column(nullable = false, length = 30)
    String telephone;
}
