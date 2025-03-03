package org.example.accountbalancemanagementfunction.exception;

public class SubscriberNotFoundException extends RuntimeException {
    public SubscriberNotFoundException() {
        super();
    }

    public SubscriberNotFoundException(String message) {
        super(message);
    }

    public SubscriberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubscriberNotFoundException(Throwable cause) {
        super(cause);
    }
}
