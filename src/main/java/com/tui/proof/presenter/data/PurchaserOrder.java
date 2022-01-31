package com.tui.proof.presenter.data;

import com.tui.proof.domain.entities.base.Address;
import com.tui.proof.domain.entities.base.Money;
import com.tui.proof.domain.entities.base.PersonalInfo;
import lombok.Builder;
import lombok.Value;
import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class PurchaserOrder{
    UUID id;
    Money total;
    Integer pilotes;
    Instant createdAt;
    Instant editableUntil;
    Address delivery;
    PersonalInfo personalInfo;

}
