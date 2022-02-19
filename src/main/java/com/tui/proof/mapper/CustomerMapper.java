package com.tui.proof.mapper;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.presenter.api.CustomerResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    void update(Customer customer, @MappingTarget Customer target);

    @Mapping(target = "id", ignore = true)
    Customer toDomain(CustomerResource item);

    CustomerResource toResource(Customer item);
}
