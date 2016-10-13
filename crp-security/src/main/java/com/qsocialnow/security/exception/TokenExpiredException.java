package com.qsocialnow.security.exception;

public class TokenExpiredException extends RuntimeException {

	private static final long serialVersionUID = -5672567588735793025L;

	public TokenExpiredException() {
	}

	public TokenExpiredException(String message) {
		super(message);
	}

	public TokenExpiredException(Throwable t) {
		super(t);
	}
}
