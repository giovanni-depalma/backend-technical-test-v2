package com.tui.proof.presenter.mapper;

import com.tui.proof.mapper.OrderMapper;
import com.tui.proof.service.api.OrderRequest;
import com.tui.proof.util.FakeOrder;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderMapperTest {

    @Test
    public void shouldOrderRequestToDomain(){
        OrderMapper mapper = Mappers.getMapper(OrderMapper.class);
        OrderRequest expected = FakeOrder.buildOrderRequest();
        OrderRequest actual = mapper.toDomain(FakeOrder.buildResource(expected));
        assertEquals(expected, actual);
    }


}
