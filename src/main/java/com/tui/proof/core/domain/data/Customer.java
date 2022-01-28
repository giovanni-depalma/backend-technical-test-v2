package com.tui.proof.core.domain.data;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Customer {
    long id;
    PersonalInfo personalInfo;
}
