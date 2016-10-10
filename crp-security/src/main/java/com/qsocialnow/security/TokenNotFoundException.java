package com.qsocialnow.security;

public class TokenNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8740180504400435370L;

	public TokenNotFoundException() {
	}

	public TokenNotFoundException(String message) {
		super(message);
	}

	public TokenNotFoundException(Throwable t) {
		super(t);
	}

}
