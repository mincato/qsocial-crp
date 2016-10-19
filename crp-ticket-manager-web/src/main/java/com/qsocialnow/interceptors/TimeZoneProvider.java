package com.qsocialnow.interceptors;

import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.util.Locales;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.util.RequestInterceptor;

import com.qsocialnow.security.AuthorizationHelper;
import com.qsocialnow.security.LoginConfigBean;
import com.qsocialnow.security.UserData;

public class TimeZoneProvider implements RequestInterceptor {

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

		try {
			authHelper.authorize(httpRequest);

		} catch (Exception e) {
			String loginUrl = findLoginUrl(httpRequest);
			redirectToLogin(loginUrl);
		}
	}

	private String findLoginUrl(HttpServletRequest httpRequest) {
		ServletContext context = httpRequest.getServletContext();
		WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);

		LoginConfigBean loginConfigBean = springContext.getBean(LoginConfigBean.class);
		String loginUrl = loginConfigBean.getLoginUrl();
		return loginUrl;
	}
	
	private void redirectToLogin(String loginUrl) {
		try {
			Execution exec = Executions.getCurrent();
			HttpServletResponse response = (HttpServletResponse) exec.getNativeResponse();
			response.sendRedirect(response.encodeRedirectURL(loginUrl));
			exec.setVoided(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
