package com.tui.proof.data.db.mapper;

import com.tui.proof.old.CustomerOld;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.old.db.entities.CustomerDataOld;
import com.tui.proof.old.db.mapper.CustomerMapper;
import com.tui.proof.util.FakeCustomer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerMapperTest {


    @Test
    public void shouldMapToPersonalInfo() {
        CustomerMapper mapper = new CustomerMapper();
        CustomerDataOld customerData = FakeCustomer.buildCustomerData();
        PersonalInfo actual = mapper.toPersonalInfo(customerData);
        assertAll(
                () -> assertEquals(customerData.getFirstName(), actual.getFirstName()),
                () -> assertEquals(customerData.getLastName(), actual.getLastName()),
                () -> assertEquals(customerData.getEmail(), actual.getEmail()),
                () -> assertEquals(customerData.getTelephone(), actual.getTelephone()));
    }

    @Test
    public void shouldMapToDomain() {
        CustomerMapper mapper = new CustomerMapper();
        CustomerDataOld customerData = FakeCustomer.buildCustomerData();
        CustomerOld actual = mapper.toDomain(customerData);
        PersonalInfo actualPersonalInfo = actual.getPersonalInfo();
        assertAll(
                () -> assertEquals(customerData.getId(), actual.getId()),
                () -> assertEquals(customerData.getFirstName(), actualPersonalInfo.getFirstName()),
                () -> assertEquals(customerData.getLastName(), actualPersonalInfo.getLastName()),
                () -> assertEquals(customerData.getEmail(), actualPersonalInfo.getEmail()),
                () -> assertEquals(customerData.getTelephone(), actualPersonalInfo.getTelephone()));
    }

    @Test
    public void shouldPopulateData() {
        CustomerMapper mapper = new CustomerMapper();
        CustomerDataOld customerData = new CustomerDataOld();
        PersonalInfo personalInfo = FakeCustomer.buildPersonalInfo();

        mapper.populateData(customerData, personalInfo);
        assertAll(
                () -> assertEquals(personalInfo.getFirstName(), customerData.getFirstName()),
                () -> assertEquals(personalInfo.getLastName(), customerData.getLastName()),
                () -> assertEquals(personalInfo.getEmail(), customerData.getEmail()),
                () -> assertEquals(personalInfo.getTelephone(), customerData.getTelephone()));
    }

}
