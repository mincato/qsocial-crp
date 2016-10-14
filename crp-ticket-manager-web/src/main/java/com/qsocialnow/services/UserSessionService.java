package com.qsocialnow.services;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.qsocialnow.security.UserData;

@Service
public class UserSessionService {

    public String getUsername() {
        UserData userData = getUserData();
        if (userData != null) {
            return userData.getUsername();
        }
        return null;
    }

    public static final String USER_SESSION_PARAMETER = "user";

    private UserData getUserData() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession httpSession = attr.getRequest().getSession(false);
        if (httpSession != null)
            return (UserData) httpSession.getAttribute(USER_SESSION_PARAMETER);
        return null;
    }
}