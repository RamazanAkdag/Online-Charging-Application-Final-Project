package com.ramobeko.accountordermanagement.util.generator;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class TelephoneNumberGenerator implements ITelephoneNumberGenerator {

    private static final String PREFIX = "555";
    private static final int NUMBER_LENGTH = 7;

    @Override
    public String generate() {
        Random random = new Random();
        int randomNumber = random.nextInt((int) Math.pow(10, NUMBER_LENGTH));

        return PREFIX + String.format("%07d", randomNumber);
    }
}