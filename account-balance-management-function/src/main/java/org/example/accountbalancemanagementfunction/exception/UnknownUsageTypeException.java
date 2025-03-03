package org.example.accountbalancemanagementfunction.exception;

public class UnknownUsageTypeException extends RuntimeException {
    public UnknownUsageTypeException() {
        super();
    }

    public UnknownUsageTypeException(String message) {
        super(message);
    }

    public UnknownUsageTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownUsageTypeException(Throwable cause) {
        super(cause);
    }
}
