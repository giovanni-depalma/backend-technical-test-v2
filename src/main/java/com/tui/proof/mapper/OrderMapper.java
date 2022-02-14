package com.tui.proof.mapper;

import com.tui.proof.domain.entities.Order;
import com.tui.proof.presenter.api.OrderRequestResource;
import com.tui.proof.presenter.api.OrderResource;
import com.tui.proof.service.api.OrderRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, CustomerMapper.class})
public interface OrderMapper {
    OrderRequest toDomain(OrderRequestResource item);

    OrderResource toResource(Order item);
}
