package com.qsocialnow.security;

public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = 334682693929423784L;

	public UnauthorizedException() {
	}

	public UnauthorizedException(String message) {
		super(message);
	}

	public UnauthorizedException(Throwable t) {
		super(t);
	}
}
