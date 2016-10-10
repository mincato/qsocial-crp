package com.qsocialnow.security;

import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.GsonBuilder;

public class ZookeeperClient {
	
	private static final long SECONDS_IN_A_MINUTE = 60;
	private static final long MILISECONDS_IN_A_SECOND = 1000;
	
	private CuratorFramework zookeeperClient;
	
	public ZookeeperClient(ServletContext context) {
		WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		zookeeperClient = springContext.getBean(CuratorFramework.class);
	}
	
	public UserActivityData findUserActivityData(String token) throws Exception {
		String path = "/sessions/" + token;
		byte[] bytes = zookeeperClient.getData().forPath(path);
		UserActivityData activity = new GsonBuilder().create().fromJson(new String(bytes), UserActivityData.class);
		return activity;
	}
	
	public void updateUserActivityData(String token, UserActivityData activity) throws Exception {		
		long newExpirationTime = calculateNewExpirationTime(activity);
		activity.setEpochExpirationTime(newExpirationTime);
		
		String path = "/sessions/" + token;
		byte[] bytes = new GsonBuilder().serializeNulls().create().toJson(activity).getBytes();
		zookeeperClient.setData().forPath(path, bytes);
	}

	private long calculateNewExpirationTime(UserActivityData activity) {
		long timeoutInMinutes = activity.getSessionTimeoutInMinutes();
		return new Date(new Date().getTime() + MILISECONDS_IN_A_SECOND * SECONDS_IN_A_MINUTE * timeoutInMinutes)
				.getTime();
	}
	
}
