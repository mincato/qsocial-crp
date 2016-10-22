package com.qsocialnow.security;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

@Service
public class SessionHandler {

    private static final Logger log = LoggerFactory.getLogger(SessionHandler.class);

    @Autowired
    private LoginConfig loginConfig;

    public void logout() {
        try {
            Execution exec = Executions.getCurrent();
            HttpServletResponse response = (HttpServletResponse) exec.getNativeResponse();
            response.sendRedirect(response.encodeRedirectURL(loginConfig.getLoginUrl()));
            exec.setVoided(true);
        } catch (IOException e) {
            log.error("logout", e);
        }

    }
}