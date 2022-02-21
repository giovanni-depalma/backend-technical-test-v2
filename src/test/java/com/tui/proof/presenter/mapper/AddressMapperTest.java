package com.tui.proof.presenter.mapper;

import com.tui.proof.domain.entities.base.Address;
import com.tui.proof.mapper.AddressMapper;
import com.tui.proof.presenter.api.AddressResource;
import com.tui.proof.util.FakeAddress;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class AddressMapperTest {

    @Test
    public void shouldToDomain(){
        AddressMapper mapper = Mappers.getMapper(AddressMapper.class);
        Address expected = FakeAddress.buildAddress();
        Address actual = mapper.toDomain(FakeAddress.buildResource(expected));
        assertEquals(expected, actual);
    }

    @Test
    public void shouldToResource(){
        AddressMapper mapper = Mappers.getMapper(AddressMapper.class);
        Address address = FakeAddress.buildAddress();
        AddressResource expected = FakeAddress.buildResource(address);
        AddressResource actual = mapper.toResource(address);
        assertEquals(expected, actual);
    }


}
