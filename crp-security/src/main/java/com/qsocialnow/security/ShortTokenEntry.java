package com.qsocialnow.security;

import java.util.Date;

public class ShortTokenEntry {

	private static final long MILISECONDS_IN_A_SECOND = 1000L;
	private static final long EXPIRATION_TIME_IN_SECONDS = 10L;

	private String token;

	private long epochExpirationTime;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setEpochExpirationTime(long epochExpirationTime) {
		this.epochExpirationTime = epochExpirationTime;
	}

	public long getEpochExpirationTime() {
		return epochExpirationTime;
	}

	public void calculateNewEpochExpirationTime() {
		epochExpirationTime = new Date().getTime() + (MILISECONDS_IN_A_SECOND * EXPIRATION_TIME_IN_SECONDS);
	}

	public boolean isExpired() {
		long now = new Date().getTime();
		return (now >= epochExpirationTime);
	}
}
