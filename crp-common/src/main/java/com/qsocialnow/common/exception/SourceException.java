package com.qsocialnow.common.exception;

public class SourceException extends RuntimeException {

    private static final long serialVersionUID = 8576704829623620122L;

    public SourceException() {
        super();
    }

    public SourceException(String message) {
        super(message);
    }

    public SourceException(Throwable cause) {
        super(cause);
    }

    public SourceException(String message, Throwable cause) {
        super(message, cause);
    }

}
