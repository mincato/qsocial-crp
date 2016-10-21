package com.qsocialnow.services;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.qsocialnow.security.UserData;

@Service
public class UserSessionService {

    public static final String USER_SESSION_PARAMETER = "user";

    public String getUsername() {
        UserData userData = getUserData();
        if (userData != null) {
            return userData.getUsername();
        }
        return null;
    }

    private UserData getUserData() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = attr.getRequest().getSession(false);
        if (httpSession != null)
            return (UserData) httpSession.getAttribute(USER_SESSION_PARAMETER);
        return null;
    }

    public String getTimeZone() {
        UserData userData = getUserData();
        if (userData != null) {
            return userData.getTimezone();
        }
        return null;
    }

    public String getLanguage() {
        UserData userData = getUserData();
        if (userData != null) {
            return userData.getLanguage();
        }
        return null;
    }

    public boolean isAdmin() {
        UserData userData = getUserData();
        if (userData != null) {
            return userData.isPrcAdmin();
        }
        return false;
    }
    
    public boolean isAnalyticsAllowed() {
        UserData userData = getUserData();
        if (userData != null) {
            return userData.isAnalyticsAllowed();
        }
        return false;
    }

}