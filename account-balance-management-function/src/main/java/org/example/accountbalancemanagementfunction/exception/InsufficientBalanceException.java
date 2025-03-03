package org.example.accountbalancemanagementfunction.exception;


public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }

}

