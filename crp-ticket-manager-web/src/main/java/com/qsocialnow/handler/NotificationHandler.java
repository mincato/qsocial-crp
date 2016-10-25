package com.qsocialnow.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericInitiator;

public class NotificationHandler extends GenericInitiator {

    private static final String NOTIFICATION_KEY = "notification";

    @Override
    public void doInit(Page page, Map<String, Object> args) throws Exception {

        HttpSession session = retrieveSession();

        if (session != null) {
            String message = (String) session.getAttribute(NOTIFICATION_KEY);
            if (StringUtils.isNotBlank(message)) {
                Clients.showNotification(message);
                session.setAttribute(NOTIFICATION_KEY, null);
            }
        }
    }

    public static void addNotification(String message) {
        HttpSession session = retrieveSession();
        if (session != null) {
            session.setAttribute(NOTIFICATION_KEY, message);
        }
    }

    private static HttpSession retrieveSession() {
        Execution exec = Executions.getCurrent();
        HttpServletRequest request = (HttpServletRequest) exec.getNativeRequest();
        return request.getSession(false);
    }
}
