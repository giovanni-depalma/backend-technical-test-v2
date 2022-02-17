package com.tui.proof.mapper;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.Order;
import com.tui.proof.presenter.api.OrderRequestResource;
import com.tui.proof.presenter.api.OrderResource;
import com.tui.proof.service.api.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, CustomerMapper.class})
public interface OrderMapper {
    @Mapping(source = "customer", target = "customer")
    OrderRequest toDomain(OrderRequestResource item, Customer customer);

    OrderRequest toDomain(OrderRequestResource item);

    OrderResource toResource(Order item);
}
