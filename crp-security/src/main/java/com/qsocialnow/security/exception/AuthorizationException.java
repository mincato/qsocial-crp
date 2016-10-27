package com.qsocialnow.security.exception;

public class AuthorizationException extends RuntimeException {

    private static final long serialVersionUID = -6108700880811513355L;

    public AuthorizationException() {
        super();
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

}
