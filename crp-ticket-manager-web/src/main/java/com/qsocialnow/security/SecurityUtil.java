package com.qsocialnow.security;

import org.apache.commons.lang3.StringUtils;

import com.qsocialnow.services.UserSessionService;

public class SecurityUtil {

    private static final String ADMIN = "ADMIN";

    public static boolean isAllGranted(String authorities) {
        boolean isGranted = false;
        if (StringUtils.isNotEmpty(authorities)) {
            UserSessionService userSessionService = new UserSessionService();
            String[] roles = authorities.split(",");
            for (String role : roles) {
                switch (role) {
                    case ADMIN:
                        isGranted = userSessionService.isAdmin();
                        break;
                    default:
                        break;
                }

            }
        }
        return isGranted;
    }

    public static boolean isAnalyticsAllowed() {
        UserSessionService userSessionService = new UserSessionService();
        return userSessionService.isAnalyticsAllowed();
    }

    public static boolean isOdatech() {
        UserSessionService userSessionService = new UserSessionService();
        return userSessionService.isOdatech();
    }

}
