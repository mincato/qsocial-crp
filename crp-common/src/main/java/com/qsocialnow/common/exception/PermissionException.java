package com.qsocialnow.common.exception;

public class PermissionException extends RuntimeException {

    private static final long serialVersionUID = -6108700880811513355L;

    public PermissionException() {
        super();
    }

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(Throwable cause) {
        super(cause);
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

}
