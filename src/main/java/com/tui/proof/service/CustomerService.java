package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.PersonalInfo;

public interface CustomerService {

    Customer findByEmailAndSave(PersonalInfo customer);

}
