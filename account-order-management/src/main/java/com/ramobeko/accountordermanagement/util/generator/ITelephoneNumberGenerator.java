package com.ramobeko.accountordermanagement.util.generator;

public interface ITelephoneNumberGenerator {
    /**
     * Generates a unique phone number based on the given user ID.
     * @return Generated phone number in string format.
     */
    String generate();
}
