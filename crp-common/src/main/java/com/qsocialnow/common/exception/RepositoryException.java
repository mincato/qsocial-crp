package com.qsocialnow.common.exception;

public class RepositoryException extends RuntimeException {

    private static final long serialVersionUID = -6108700880811513355L;

    public RepositoryException() {
        super();
    }

    public RepositoryException(String message) {
        super(message);
    }

    public RepositoryException(Throwable cause) {
        super(cause);
    }

    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

}
