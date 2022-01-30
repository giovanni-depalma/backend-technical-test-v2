package com.tui.proof.presenter.data;

import com.tui.proof.domain.entities.Address;
import com.tui.proof.domain.entities.Money;
import com.tui.proof.domain.entities.PersonalInfo;
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
