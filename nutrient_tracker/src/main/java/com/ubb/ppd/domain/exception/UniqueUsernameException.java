package com.ubb.ppd.domain.exception;

public class UniqueUsernameException extends RuntimeException {
    public UniqueUsernameException(String message) {
        super(message);
    }
}
