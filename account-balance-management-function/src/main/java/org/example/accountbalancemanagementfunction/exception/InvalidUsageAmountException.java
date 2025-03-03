package org.example.accountbalancemanagementfunction.exception;

public class InvalidUsageAmountException extends RuntimeException {
    public InvalidUsageAmountException() {
        super();
    }

    public InvalidUsageAmountException(String message) {
        super(message);
    }

    public InvalidUsageAmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUsageAmountException(Throwable cause) {
        super(cause);
    }
}
