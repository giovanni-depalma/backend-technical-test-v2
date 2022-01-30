package com.tui.proof.old.db.mapper;


import com.tui.proof.old.CustomerOld;
import com.tui.proof.domain.entities.PersonalInfo;
import com.tui.proof.old.db.entities.CustomerDataOld;
import org.springframework.stereotype.Component;


@Component
public class CustomerMapper {

    public CustomerOld toDomain(CustomerDataOld customerData) {
        PersonalInfo personalInfo = toPersonalInfo(customerData);
        return CustomerOld.builder().id(customerData.getId()).personalInfo(personalInfo).build();
    }

    public PersonalInfo toPersonalInfo(CustomerDataOld customerData) {
        return PersonalInfo.builder().email(customerData.getEmail()).firstName(customerData.getFirstName())
                .lastName(customerData.getLastName()).telephone(customerData.getTelephone()).build();
    }

    public void populateData(CustomerDataOld customerData, PersonalInfo customer) {
        customerData.setFirstName(customer.getFirstName());
        customerData.setLastName(customer.getLastName());
        customerData.setTelephone(customer.getTelephone());
        customerData.setEmail(customer.getEmail());
    }

}
