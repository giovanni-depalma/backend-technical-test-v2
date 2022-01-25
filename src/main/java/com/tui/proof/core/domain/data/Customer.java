package com.tui.proof.core.domain.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {
    private long id;
    private PersonalInfo personalInfo;
}
