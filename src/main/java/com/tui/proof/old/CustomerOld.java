package com.tui.proof.old;

import com.tui.proof.domain.entities.PersonalInfo;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOld {
    long id;
    @Embedded
    PersonalInfo personalInfo;
}
