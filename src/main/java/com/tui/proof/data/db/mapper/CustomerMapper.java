package com.tui.proof.data.db.mapper;


import com.tui.proof.core.domain.data.Customer;
import com.tui.proof.core.domain.data.PersonalInfo;
import com.tui.proof.data.db.entities.CustomerData;
import org.springframework.stereotype.Component;


@Component
public class CustomerMapper {

    public Customer toDomain(CustomerData customerData) {
        PersonalInfo personalInfo = toPersonalInfo(customerData);
        return Customer.builder().id(customerData.getId()).personalInfo(personalInfo).build();
    }

    public PersonalInfo toPersonalInfo(CustomerData customerData) {
        return PersonalInfo.builder().email(customerData.getEmail()).firstName(customerData.getFirstName())
                .lastName(customerData.getLastName()).telephone(customerData.getTelephone()).build();
    }

    public void populateData(CustomerData customerData, PersonalInfo customer) {
        customerData.setFirstName(customer.getFirstName());
        customerData.setLastName(customer.getLastName());
        customerData.setTelephone(customer.getTelephone());
        customerData.setEmail(customer.getEmail());
    }

}
