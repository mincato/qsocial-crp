package com.qsocialnow.security;

import java.text.MessageFormat;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.CharEncoding;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.GsonBuilder;

public class ZookeeperClient {

	private static final String ZOOKEPEER_TOKEN_PATH = "/tokens/{0}";
	private static final String ZOOKEPEER_SESSION_PATH = "/sessions/{0}";

	private CuratorFramework zookeeperClient;

	public ZookeeperClient(ServletContext context) {
		WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		zookeeperClient = (CuratorFramework) springContext.getBean("zookeeperClient");
	}

	public UserActivityData findUserActivityData(String token) throws Exception {
		String path = MessageFormat.format(ZOOKEPEER_SESSION_PATH, token);
		byte[] bytes = zookeeperClient.getData().forPath(path);
		UserActivityData activity = new GsonBuilder().create().fromJson(new String(bytes, CharEncoding.UTF_8),
				UserActivityData.class);
		return activity;
	}

	public void updateUserActivityData(String token, UserActivityData activity) throws Exception {
		activity.calculateNewEpochExpirationTime();

		String path = MessageFormat.format(ZOOKEPEER_SESSION_PATH, token);
		byte[] bytes = new GsonBuilder().serializeNulls().create().toJson(activity).getBytes(CharEncoding.UTF_8);
		zookeeperClient.setData().forPath(path, bytes);
	}

	public ShortTokenEntry findToken(String shortToken) throws Exception {
		String path = MessageFormat.format(ZOOKEPEER_TOKEN_PATH, shortToken);
		byte[] bytes = zookeeperClient.getData().forPath(path);
		ShortTokenEntry entry = new GsonBuilder().create().fromJson(new String(bytes, CharEncoding.UTF_8),
				ShortTokenEntry.class);
		return entry;
	}

	public void removeToken(String shortToken) throws Exception {
		String path = MessageFormat.format(ZOOKEPEER_TOKEN_PATH, shortToken);
		zookeeperClient.delete().forPath(path);
	}

	public void removeSession(String token) throws Exception {
		String path = MessageFormat.format(ZOOKEPEER_SESSION_PATH, token);
		zookeeperClient.delete().forPath(path);
	}
}
