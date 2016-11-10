package com.qsocialnow.services.impl;

import java.text.MessageFormat;
import java.util.UUID;

import org.apache.commons.lang3.CharEncoding;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkoss.zk.ui.Executions;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.config.AnalyticsConfig;
import com.qsocialnow.security.ShortTokenEntry;
import com.qsocialnow.security.UserActivityData;
import com.qsocialnow.services.UserSessionService;

@Service
public class AnalyticsAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsAuthService.class);

    @Autowired
    private AnalyticsConfig analyticsConfig;

    @Autowired
    private CuratorFramework zookeeperLogin;

    @Autowired
    private UserSessionService userSessionService;

    private final static String ZOOKEEPER_PATH_TOKENS = "/tokens/{0}";
    private final static String ZOOKEEPER_PATH_SESSIONS = "/sessions/{0}";

    private final static String URL_PATTERN = "{0}?_token={1}";

    public void redirectAnalyticsHome() {
        String shortToken = UUID.randomUUID().toString();
        String token = userSessionService.createToken();
        persistShortToken(zookeeperLogin, shortToken, token);
        persistUserActivityData(zookeeperLogin, token);
        String url = MessageFormat.format(URL_PATTERN, analyticsConfig.getHomeUrl(), shortToken);
        Executions.sendRedirect(url);
    }

    private void persistShortToken(CuratorFramework client, String shortToken, String token) {
        try {
            ShortTokenEntry entry = new ShortTokenEntry();
            entry.setToken(token);
            entry.calculateNewEpochExpirationTime();

            byte[] bytes = new GsonBuilder().serializeNulls().create().toJson(entry).getBytes(CharEncoding.UTF_8);
            client.create().forPath(MessageFormat.format(ZOOKEEPER_PATH_TOKENS, shortToken), bytes);
        } catch (Exception e) {
            LOGGER.error("persist short token", e);
            throw new RuntimeException(e);
        }
    }

    private void persistUserActivityData(CuratorFramework client, String token) {
        try {
            UserActivityData activity = new UserActivityData();
            activity.setSessionTimeoutInMinutes(userSessionService.getExpirationInMinutes());
            activity.calculateNewEpochExpirationTime();

            byte[] bytes = new GsonBuilder().serializeNulls().create().toJson(activity).getBytes(CharEncoding.UTF_8);
            client.create().forPath(MessageFormat.format(ZOOKEEPER_PATH_SESSIONS, token), bytes);
        } catch (Exception e) {
            LOGGER.error("persist user activity data", e);
            throw new RuntimeException(e);
        }
    }

}
