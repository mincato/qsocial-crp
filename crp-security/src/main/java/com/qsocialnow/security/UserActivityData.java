package com.qsocialnow.security;

public class UserActivityData {
	
	private long epochExpirationTime;

	private long sessionTimeoutInMinutes;
	
	public long getSessionTimeoutInMinutes() {
		return sessionTimeoutInMinutes;
	}
	
	public void setSessionTimeoutInMinutes(long sessionTimeoutInMinutes) {
		this.sessionTimeoutInMinutes = sessionTimeoutInMinutes;
	}
	
	public long getEpochExpirationTime() {
		return epochExpirationTime;
	}
	
	public void setEpochExpirationTime(long epochExpirationTime) {
		this.epochExpirationTime = epochExpirationTime;
	}
}
