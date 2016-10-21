package com.qsocialnow.security;

import java.text.MessageFormat;

import org.apache.commons.lang3.CharEncoding;
import org.apache.curator.framework.CuratorFramework;

import com.google.gson.GsonBuilder;

public class ZookeeperClient {

	private static final String ZOOKEPEER_TOKEN_PATH = "/tokens/{0}";
	private static final String ZOOKEPEER_SESSION_PATH = "/sessions/{0}";

	private final CuratorFramework curatorFramework;
	
	public ZookeeperClient(CuratorFramework curatorFramework) {
		this.curatorFramework = curatorFramework;
	}

	public UserActivityData findUserActivityData(String token) throws Exception {
		String path = MessageFormat.format(ZOOKEPEER_SESSION_PATH, token);
		byte[] bytes = curatorFramework.getData().forPath(path);
		UserActivityData activity = new GsonBuilder().create().fromJson(new String(bytes, CharEncoding.UTF_8),
				UserActivityData.class);
		return activity;
	}

	public void updateUserActivityData(String token, UserActivityData activity) throws Exception {
		activity.calculateNewEpochExpirationTime();

		String path = MessageFormat.format(ZOOKEPEER_SESSION_PATH, token);
		byte[] bytes = new GsonBuilder().serializeNulls().create().toJson(activity).getBytes(CharEncoding.UTF_8);
		curatorFramework.setData().forPath(path, bytes);
	}

	public ShortTokenEntry findToken(String shortToken) throws Exception {
		String path = MessageFormat.format(ZOOKEPEER_TOKEN_PATH, shortToken);
		byte[] bytes = curatorFramework.getData().forPath(path);
		ShortTokenEntry entry = new GsonBuilder().create().fromJson(new String(bytes, CharEncoding.UTF_8),
				ShortTokenEntry.class);
		return entry;
	}

	public void removeToken(String shortToken) throws Exception {
		String path = MessageFormat.format(ZOOKEPEER_TOKEN_PATH, shortToken);
		curatorFramework.delete().forPath(path);
	}

	public void removeSession(String token) throws Exception {
		String path = MessageFormat.format(ZOOKEPEER_SESSION_PATH, token);
		curatorFramework.delete().forPath(path);
	}
}
