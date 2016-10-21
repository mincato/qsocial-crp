package com.qsocialnow.interceptors;

import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.curator.framework.CuratorFramework;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.util.Locales;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.util.RequestInterceptor;

import com.qsocialnow.security.AuthorizationHelper;
import com.qsocialnow.security.UserData;

public class UserConfigurator implements RequestInterceptor {
	
	private static final Logger LOGGER = Logger.getLogger(UserConfigurator.class);

	@Override
	public void request(Session session, Object request, Object response) {

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		CuratorFramework loginZookeeper = findZookeeperLogin(httpRequest);

		try {
			AuthorizationHelper authHelper = new AuthorizationHelper();
			boolean userSaved = authHelper.saveUserInSession(httpRequest, loginZookeeper);
	
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
		} catch (Exception e) {
			LOGGER.error("There was an error configurating the user in the session", e);
		}
	}

	private CuratorFramework findZookeeperLogin(HttpServletRequest request) {
		ServletContext context = request.getServletContext();
		WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		return (CuratorFramework) springContext.getBean("zookeeperLogin");
	}

}
