package com.ramobeko.accountordermanagement.service.abstrct.ignite;


import com.ramobeko.accountordermanagement.model.shared.OracleSubscriber;
import com.ramobeko.ignite.IgniteSubscriber;

public interface IIgniteSubscriberService{

    /**
     * Saves a subscriber from Oracle into Ignite.
     * This ensures that newly created Oracle subscribers are also stored in Ignite.
     *
     * @param oracleSubscriber The subscriber created in Oracle.
     */
    void createFromOracle(OracleSubscriber oracleSubscriber);

    /**
     * Updates an existing subscriber in Ignite based on Oracle data.
     * This keeps Ignite in sync with any updates made in Oracle.
     *
     * @param oracleSubscriber The updated subscriber from Oracle.
     */
    void updateFromOracle(OracleSubscriber oracleSubscriber);

    /**
     * Retrieves a subscriber from Ignite by its ID.
     *
     * @param id The ID of the subscriber.
     * @return An Optional containing the IgniteSubscriber if found.
     */
    IgniteSubscriber getById(Long id);

    /**
     * Retrieves a subscriber from Ignite by phone number.
     *
     * @param phoneNumber The phone number of the subscriber.
     * @return The IgniteSubscriber associated with the given phone number.
     */
    IgniteSubscriber getByPhoneNumber(String phoneNumber);

    public void delete(Long id);
}
