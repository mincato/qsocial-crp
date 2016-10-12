package co.centauri.web;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.CharEncoding;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;

import com.google.gson.GsonBuilder;

import co.centauri.security.TokenHandler;
import co.centauri.security.TokenHandlerFactory;
import co.centauri.security.TokenHandlerStandaloneFactory;
import co.centauri.security.UserActivityData;
import co.centauri.security.UserData;

public class Login extends HttpServlet {

	private static final long serialVersionUID = 1627012520199702295L;

	private static final String ADMIN = "administrador";

	private static final String ODATECH = "odatech";

	private static final Logger LOGGER = Logger.getLogger(Login.class);

	private static final String URL_PATTERN = "{0}://{1}:{2}/{3}?token={4}";

	private TokenHandler tokenHandler;

	private LoginProperties loginProperties;

	@Override
	public void init() throws ServletException {
		TokenHandlerFactory factory = new TokenHandlerStandaloneFactory();
		tokenHandler = factory.create();

		loginProperties = new LoginProperties();
		loginProperties.load();
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");

		UserData user = new UserData();
		user.setUsername(username);
		user.setOdatech(ODATECH.compareToIgnoreCase(username) == 0);
		user.setPrcAdmin(ADMIN.compareToIgnoreCase(username) == 0);

		String shortToken = UUID.randomUUID().toString();
		String token = tokenHandler.createToken(user);

		CuratorFramework client = createZookeeperClient();
		persistShortToken(client, shortToken, token);
		persistUserActivityData(client, token);

		String url = getCrpTicketManagerWebUrl(req, shortToken);
		resp.sendRedirect(url);
	}

	public String getCrpTicketManagerWebUrl(HttpServletRequest request, String shortToken) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		String serverPort = Integer.valueOf(request.getServerPort()).toString();
		String contextPath = "crp-ticket-manager-web";

		return MessageFormat.format(URL_PATTERN, scheme, serverName, serverPort, contextPath, shortToken);
	}

	private CuratorFramework createZookeeperClient() {
		CuratorFramework client = CuratorFrameworkFactory.newClient(loginProperties.getZookeeperHost(),
				new ExponentialBackoffRetry(1000, 3));
		client.start();
		return client;
	}

	private void persistShortToken(CuratorFramework client, String shortToken, String token) {
		try {
			client.create().forPath(MessageFormat.format(loginProperties.getZookeeperPathTokens(), shortToken),
					token.getBytes(CharEncoding.UTF_8));
		} catch (Exception e) {
			LOGGER.error(e);
			throw new RuntimeException(e);
		}
	}

	private void persistUserActivityData(CuratorFramework client, String token) {
		try {
			UserActivityData activity = new UserActivityData();
			activity.setSessionTimeoutInMinutes(loginProperties.getTimeoutInMinutes());
			activity.calculateNewEpochExpirationTime();

			byte[] bytes = new GsonBuilder().serializeNulls().create().toJson(activity).getBytes(CharEncoding.UTF_8);
			client.create().forPath(MessageFormat.format(loginProperties.getZookeeperPathSessions(), token), bytes);
		} catch (Exception e) {
			LOGGER.error(e);
			throw new RuntimeException(e);
		}
	}
}