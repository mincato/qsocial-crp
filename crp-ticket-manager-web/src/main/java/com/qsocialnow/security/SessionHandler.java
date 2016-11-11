package com.qsocialnow.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Executions;

@Service
public class SessionHandler {

    @Autowired
    private LoginConfig loginConfig;

    public void logout() {
        Executions.getCurrent().getSession().invalidate();
        Executions.sendRedirect(loginConfig.getLoginUrl());
    }
}