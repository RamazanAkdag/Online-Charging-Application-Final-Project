package org.example.accountbalancemanagementfunction.exception;

public class BalanceNotFoundException extends RuntimeException {
    public BalanceNotFoundException() {
        super();
    }

    public BalanceNotFoundException(String message) {
        super(message);
    }

    public BalanceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BalanceNotFoundException(Throwable cause) {
        super(cause);
    }
}
