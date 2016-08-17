package com.qsocialnow.kafka.exceptions;

public class EncodingException extends RuntimeException {

    private static final long serialVersionUID = 3671515205525892823L;

    public EncodingException() {
        super();
    }

    public EncodingException(String message) {
        super(message);
    }

    public EncodingException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public EncodingException(Throwable throwable) {
        super(throwable);
    }

}
