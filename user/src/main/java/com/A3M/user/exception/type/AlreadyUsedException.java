package com.A3M.user.exception.type;

public class AlreadyUsedException extends RuntimeException {
    public AlreadyUsedException(String message) {
        super(message);
    }
}
