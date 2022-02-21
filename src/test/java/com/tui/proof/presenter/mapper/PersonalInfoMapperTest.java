package com.tui.proof.presenter.mapper;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.mapper.CustomerMapper;
import com.tui.proof.presenter.api.CustomerResource;
import com.tui.proof.util.FakeCustomer;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalInfoMapperTest {

    @Test
    public void shouldToDomain(){
        CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);
        Customer expected = FakeCustomer.buildCustomer();
        Customer actual = mapper.toDomain(FakeCustomer.buildResource(expected));
        assertEquals(expected, actual);
    }

    @Test
    public void shouldToResource(){
        CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);
        Customer customer = FakeCustomer.buildCustomer();
        CustomerResource expected = FakeCustomer.buildResource(customer);
        CustomerResource actual = mapper.toResource(customer);
        assertEquals(expected, actual);
    }


}
