package com.qsocialnow.security.exception;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6108700880811513355L;

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
