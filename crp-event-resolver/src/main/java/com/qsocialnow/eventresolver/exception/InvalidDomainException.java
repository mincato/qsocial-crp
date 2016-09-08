package com.qsocialnow.eventresolver.exception;

public class InvalidDomainException extends RuntimeException {

    private static final long serialVersionUID = 6336620363356599983L;

    public InvalidDomainException() {
    }

    public InvalidDomainException(String message) {
        super(message);
    }

}
