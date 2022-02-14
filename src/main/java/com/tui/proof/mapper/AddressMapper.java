package com.tui.proof.mapper;

import com.tui.proof.domain.entities.base.Address;
import com.tui.proof.presenter.api.AddressResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toDomain(AddressResource item);

    AddressResource toResource(Address item);
}
