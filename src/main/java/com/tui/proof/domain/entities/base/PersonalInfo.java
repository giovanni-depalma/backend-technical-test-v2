package com.tui.proof.domain.entities.base;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonalInfo {

    @Schema(example = "EufrasioAlmanzaCepeda@armyspy.com")
    @Email
    @Size(min = 1, max = 100)
    @Column(unique = true, length = 100, nullable = false)
    String email;

    @Schema(example = "Eufrasio Almanza")
    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    String firstName;

    @Schema(example = "Cepeda")
    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    String lastName;

    @Schema(example = "214-474-0425")
    @NotBlank
    @Size(min = 1, max = 30)
    @Column(nullable = false, length = 30)
    String telephone;
}
