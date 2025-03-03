package org.example.exception;

public class UsageRequestException extends RuntimeException {

    public UsageRequestException() {
        super();
    }

    public UsageRequestException(String message) {
        super(message);
    }

    public UsageRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsageRequestException(Throwable cause) {
        super(cause);
    }
}

