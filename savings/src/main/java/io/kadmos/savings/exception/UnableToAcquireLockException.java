package io.kadmos.savings.exception;

public class UnableToAcquireLockException extends RuntimeException {

    public UnableToAcquireLockException(String message) {
        super(message);
    }

    public UnableToAcquireLockException(String message, Throwable cause) {
        super(message, cause);
    }

}
