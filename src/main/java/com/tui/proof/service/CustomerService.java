package com.tui.proof.service;

import com.tui.proof.domain.entities.Customer;
import com.tui.proof.domain.entities.base.PersonalInfo;

public interface CustomerService {

    Customer findByEmailAndSave(PersonalInfo customer);

}
