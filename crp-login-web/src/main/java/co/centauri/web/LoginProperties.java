package co.centauri.web;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class LoginProperties {

	private final static String ZOOKEEPER_HOST_KEY = "zookeeper.host";
	private final static String TIMEOUT_MINUTES = "timeout.minutes";
	private final static String ZOOKEEPER_PATH_TOKENS = "zookeeper.path.tokens";
	private final static String ZOOKEEPER_PATH_SESSIONS = "zookeeper.path.sessions";

	private Properties properties;

	public void load() {
		try {
			String directory = System.getenv("extPropDir");
			if (directory.startsWith("file:")) {
				directory = directory.substring("file:".length());
			}
			String absolutePath = directory + "login.properties";

			properties = new Properties();
			properties.load(new FileInputStream(new File(absolutePath)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getZookeeperHost() {
		return properties.getProperty(ZOOKEEPER_HOST_KEY);
	}

	public long getTimeoutInMinutes() {
		String timeout = properties.getProperty(TIMEOUT_MINUTES);
		return Long.parseLong(timeout);
	}

	public String getZookeeperPathTokens() {
		return properties.getProperty(ZOOKEEPER_PATH_TOKENS);
	}

	public String getZookeeperPathSessions() {
		return properties.getProperty(ZOOKEEPER_PATH_SESSIONS);
	}
}
