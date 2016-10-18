package com.qsocialnow.interceptors;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.zkoss.util.Locales;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.util.RequestInterceptor;

import com.qsocialnow.security.AuthorizationFilter;
import com.qsocialnow.security.UserData;

public class TimeZoneProvider implements RequestInterceptor {

    @Override
    public void request(Session session, Object request, Object response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession httpSession = httpRequest.getSession(false);
        if (httpSession != null) {
            UserData userData = (UserData) httpSession.getAttribute(AuthorizationFilter.USER_SESSION_PARAMETER);
            if (userData != null && userData.getTimezone() != null) {
                session.setAttribute(Attributes.PREFERRED_TIME_ZONE, TimeZone.getTimeZone(userData.getTimezone()));

            }
            if (userData != null && userData.getLanguage() != null) {
                session.setAttribute(Attributes.PREFERRED_LOCALE, Locales.getLocale(userData.getLanguage()));
            }
        }
    }

}
