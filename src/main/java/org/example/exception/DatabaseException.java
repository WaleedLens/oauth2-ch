package org.example.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
    public DatabaseException(Throwable cause) {
        super(cause);
    }
}
