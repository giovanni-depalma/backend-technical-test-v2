package com.tui.proof.data.db.mapper;

import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.data.db.entities.CustomerData;
import com.tui.proof.util.FakeCustomer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerMapperTest {


    @Test
    public void shouldMapToPersonalInfo() {
        CustomerMapper mapper = new CustomerMapper();
        CustomerData customerData = FakeCustomer.buildCustomerData();
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
        CustomerData customerData = FakeCustomer.buildCustomerData();
        Customer actual = mapper.toDomain(customerData);
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
        CustomerData customerData = new CustomerData();
        PersonalInfo personalInfo = FakeCustomer.buildPersonalInfo();

        mapper.populateData(customerData, personalInfo);
        assertAll(
                () -> assertEquals(personalInfo.getFirstName(), customerData.getFirstName()),
                () -> assertEquals(personalInfo.getLastName(), customerData.getLastName()),
                () -> assertEquals(personalInfo.getEmail(), customerData.getEmail()),
                () -> assertEquals(personalInfo.getTelephone(), customerData.getTelephone()));
    }

}
