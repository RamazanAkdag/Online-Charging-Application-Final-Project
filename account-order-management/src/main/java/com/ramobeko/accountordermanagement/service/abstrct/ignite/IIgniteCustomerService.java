package com.ramobeko.accountordermanagement.service.abstrct.ignite;

import com.ramobeko.accountordermanagement.model.entity.ignite.IgniteCustomer;
import com.ramobeko.accountordermanagement.model.entity.oracle.OracleCustomer;

public interface IIgniteCustomerService extends IIgniteService<IgniteCustomer> {

    /**
     * Registers a new customer using the provided registration details
     * and returns the created Customer entity.
     *
     * @param igniteCustomer object.
     * @return the registered Customer entity.
     */
    IgniteCustomer register(IgniteCustomer igniteCustomer);
}
