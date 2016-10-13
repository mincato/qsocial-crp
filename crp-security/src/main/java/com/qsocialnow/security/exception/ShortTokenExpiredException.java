package com.qsocialnow.security.exception;

public class ShortTokenExpiredException extends RuntimeException {

	private static final long serialVersionUID = -5672567588735793025L;

	public ShortTokenExpiredException() {
	}

	public ShortTokenExpiredException(String message) {
		super(message);
	}

	public ShortTokenExpiredException(Throwable t) {
		super(t);
	}
}
