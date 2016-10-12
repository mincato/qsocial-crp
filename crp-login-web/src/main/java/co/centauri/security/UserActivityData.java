package co.centauri.security;

import java.util.Date;

public class UserActivityData {

	private static final long SECONDS_IN_A_MINUTE = 60;
	private static final long MILISECONDS_IN_A_SECOND = 1000;

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

	public void calculateNewEpochExpirationTime() {
		epochExpirationTime = new Date(
				new Date().getTime() + MILISECONDS_IN_A_SECOND * SECONDS_IN_A_MINUTE * sessionTimeoutInMinutes)
						.getTime();
	}

	public boolean isExpired() {
		long now = new Date().getTime();
		return (now >= epochExpirationTime);
	}

}
