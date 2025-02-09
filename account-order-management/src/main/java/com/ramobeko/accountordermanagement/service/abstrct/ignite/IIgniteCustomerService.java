package com.ramobeko.accountordermanagement.service.abstrct.ignite;

import com.ramobeko.accountordermanagement.model.dto.AuthRequest;
import com.ramobeko.accountordermanagement.model.dto.RegisterRequest;
import com.ramobeko.accountordermanagement.model.entity.Customer;

public interface IIgniteCustomerService extends IIgniteService<Customer> {

    void register(RegisterRequest request);
}
