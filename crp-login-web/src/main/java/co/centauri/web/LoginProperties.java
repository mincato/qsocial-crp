package co.centauri.web;

import java.io.File;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class LoginProperties {

	private interface Keys {
		final static String ZOOKEEPER_HOST = "zookeeper.host";
		final static String TIMEOUT_MINUTES = "timeout.minutes";
		final static String ZOOKEEPER_PATH_TOKENS = "zookeeper.path.tokens";
		final static String ZOOKEEPER_PATH_SESSIONS = "zookeeper.path.sessions";
	}

	private interface DefaultValues {
		final static String ZOOKEEPER_HOST = "localhost:2182";
		final static long TIMEOUT_MINUTES = 10L;
		final static String ZOOKEEPER_PATH_TOKENS = "/tokens/{0}";
		final static String ZOOKEEPER_PATH_SESSIONS = "/sessions/{0}";
	}

	private static final Logger LOGGER = Logger.getLogger(LoginProperties.class);

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
			properties = null;
			LOGGER.info("=====> login.properties file not found: using default values:");
			LOGGER.info(MessageFormat.format("{0}: {1}", Keys.ZOOKEEPER_HOST, DefaultValues.ZOOKEEPER_HOST));
			LOGGER.info(MessageFormat.format("{0}: {1}", Keys.TIMEOUT_MINUTES, DefaultValues.TIMEOUT_MINUTES));
			LOGGER.info(
					MessageFormat.format("{0}: {1}", Keys.ZOOKEEPER_PATH_TOKENS, DefaultValues.ZOOKEEPER_PATH_TOKENS));
			LOGGER.info(MessageFormat.format("{0}: {1}", Keys.ZOOKEEPER_PATH_SESSIONS,
					DefaultValues.ZOOKEEPER_PATH_SESSIONS));
		}
	}

	public String getZookeeperHost() {
		return getString(Keys.ZOOKEEPER_HOST, DefaultValues.ZOOKEEPER_HOST);
	}

	public long getTimeoutInMinutes() {
		return getLong(Keys.TIMEOUT_MINUTES, DefaultValues.TIMEOUT_MINUTES);
	}

	public String getZookeeperPathTokens() {
		return getString(Keys.ZOOKEEPER_PATH_TOKENS, DefaultValues.ZOOKEEPER_PATH_TOKENS);
	}

	public String getZookeeperPathSessions() {
		return getString(Keys.ZOOKEEPER_PATH_SESSIONS, DefaultValues.ZOOKEEPER_PATH_SESSIONS);
	}

	private String getString(String key, String defaultValue) {
		String value = null;
		if (properties != null) {
			value = properties.getProperty(key);
		}
		return (StringUtils.isBlank(value)) ? defaultValue : value;
	}

	private long getLong(String key, long defaultValue) {
		Long value = null;
		try {
			String valueStr = properties.getProperty(key);
			value = Long.parseLong(valueStr);
		} catch (Exception e) {
			value = defaultValue;
		}
		return value;
	}
}
