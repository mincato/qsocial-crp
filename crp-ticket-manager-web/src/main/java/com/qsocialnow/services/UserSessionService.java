package com.qsocialnow.services;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.qsocialnow.security.TokenHandler;
import com.qsocialnow.security.UserData;
import com.qsocialnow.security.exception.AuthorizationException;

@Service
public class UserSessionService {

	public static final String USER_SESSION_PARAMETER = "user";

	@Autowired
	private TokenHandler tokenHandler;

	public String getUsername() {
		UserData userData = getUserData();
		if (userData == null) {
			throw new AuthorizationException();
		}
		return userData.getUsername();
	}

	public String getTimeZone() {
		UserData userData = getUserData();
		if (userData == null) {
			throw new AuthorizationException();
		}
		return userData.getTimezone();
	}

	public String getLanguage() {
		UserData userData = getUserData();
		if (userData == null) {
			throw new AuthorizationException();
		}
		return userData.getLanguage();
	}

	public boolean isAdmin() {
		UserData userData = getUserData();
		if (userData == null) {
			throw new AuthorizationException();
		}
		return userData.isPrcAdmin();
	}

	public boolean isAnalyticsAllowed() {
		UserData userData = getUserData();
		if (userData == null) {
			throw new AuthorizationException();
		}
		return userData.isAnalyticsAllowed();
	}

	public String createToken() {
		return tokenHandler.createToken(getUserData());
	}

	public int getExpirationInMinutes() {
		return tokenHandler.getExpirationInMinutes();
	}

	private UserData getUserData() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession httpSession = attr.getRequest().getSession(false);
		if (httpSession != null) {
			return (UserData) httpSession.getAttribute(USER_SESSION_PARAMETER);
		}
		return null;
	}

}