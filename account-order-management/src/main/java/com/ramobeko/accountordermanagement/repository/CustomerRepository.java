package com.ramobeko.accountordermanagement.repository;

import com.ramobeko.accountordermanagement.model.entity.oracle.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Procedure(procedureName = "AOM.PKG_CUSTOMER.ADD_CUSTOMER")
    void addCustomer(String p_cust_name, String p_cust_mail, String p_cust_address, String p_cust_status);


    @Procedure(procedureName = "AOM.PKG_CUSTOMER.GET_ALL_CUSTOMERS")
    List<Customer> getAllCustomers();

    Optional<Customer> findByEmail(String email);
}