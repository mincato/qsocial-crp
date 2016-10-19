package com.qsocialnow.interceptors;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkoss.util.Locales;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.util.RequestInterceptor;

import com.qsocialnow.security.AuthorizationHelper;
import com.qsocialnow.security.UserData;

public class UserConfigurator implements RequestInterceptor {

	@Override
	public void request(Session session, Object request, Object response) {

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		AuthorizationHelper authHelper = new AuthorizationHelper();
		boolean userSaved = authHelper.saveUserInSession(httpRequest);

		if (userSaved) {
			HttpSession httpSession = httpRequest.getSession(false);
			UserData userData = (UserData) httpSession.getAttribute(AuthorizationHelper.USER_SESSION_PARAMETER);
			if (userData != null && userData.getTimezone() != null) {
				session.setAttribute(Attributes.PREFERRED_TIME_ZONE, TimeZone.getTimeZone(userData.getTimezone()));

			}
			if (userData != null && userData.getLanguage() != null) {
				session.setAttribute(Attributes.PREFERRED_LOCALE, Locales.getLocale(userData.getLanguage()));
			}
		}
	}
}
