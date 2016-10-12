package com.qsocialnow.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.qsocialnow.security.exception.SessionExpiredException;
import com.qsocialnow.security.exception.ShortTokenExpiredException;
import com.qsocialnow.security.exception.TokenNotFoundException;

public class AuthorizationFilter implements Filter {

	private static final Logger LOGGER = Logger.getLogger(AuthorizationFilter.class);

	private static final String URL_LOGIN_INIT_PARAMETER = "urlLogin";
	private static final String TOKEN_REQUEST_PARAMETER = "token";
	private static final String TOKEN_SESSION_PARAMETER = "token";
	private static final String USER_SESSION_PARAMETER = "user";
	private static final String SHORT_TOKEN_SESSION_PARAMETER = "shortToken";

	private String urlLogin;

	@Override
	public void destroy() {
		LOGGER.info("=== Destroying AuthorizationFilter ===");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		try {
			ServletContext context = request.getServletContext();
			TokenHandlerFactory factory = createTokenHandlerFactory(context);
			TokenHandler tokenHandler = factory.create();
			ZookeeperClient zookeeperClient = new ZookeeperClient(context);
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpSession session = httpRequest.getSession(true);

			String token = findTokenFromRequestParam(zookeeperClient, httpRequest);

			if (StringUtils.isBlank(token)) {
				token = (String) session.getAttribute(TOKEN_SESSION_PARAMETER);
			}

			if (StringUtils.isBlank(token)) {
				throw new TokenNotFoundException();
			}

			UserData user = tokenHandler.verifyToken(token);

			UserActivityData activity = zookeeperClient.findUserActivityData(token);
			verifyExpiration(activity);

			session.setMaxInactiveInterval((int) activity.getSessionTimeoutInSeconds());
			session.setAttribute(TOKEN_SESSION_PARAMETER, token);
			session.setAttribute(USER_SESSION_PARAMETER, user);
			zookeeperClient.updateUserActivityData(token, activity);

		} catch (Exception e) {
			LOGGER.error("Error authorizing token", e);
			redirectToLogin(response);
		}
		
		chain.doFilter(request, response);
	}

	private String findTokenFromRequestParam(ZookeeperClient zookeeperClient, HttpServletRequest httpRequest)
			throws Exception {

		String token = null;
		String shortToken = httpRequest.getParameter(TOKEN_REQUEST_PARAMETER);
		if (StringUtils.isNotBlank(shortToken)) {
			
			// Se busca si el short token ya fue usado y si esta en la sesion, para no ir a zookeeper
			HttpSession session = httpRequest.getSession();
			String shortTokenFromSession = (String) session.getAttribute(SHORT_TOKEN_SESSION_PARAMETER);
			if (shortToken.equals(shortTokenFromSession)) {
				return (String) session.getAttribute(TOKEN_SESSION_PARAMETER);
			}
			
			// Si es un short token nuevo hay que ir a buscarlo a zookeeper
			ShortTokenEntry entry = zookeeperClient.findToken(shortToken);
			if (entry.isExpired()) {
				throw new ShortTokenExpiredException();
			}
			token = entry.getToken();
			zookeeperClient.removeToken(shortToken);
			
			// El short token se remueve de zookeeper para evitar accesos foraneos, y se guarda en la sesion
			session.setAttribute(SHORT_TOKEN_SESSION_PARAMETER, shortToken);
		}
		return token;
	}

	private void verifyExpiration(UserActivityData activity) {
		if (activity.isExpired()) {
			throw new SessionExpiredException();
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		LOGGER.info("=== Initializing AuthorizationFilter ===");

		urlLogin = config.getInitParameter(URL_LOGIN_INIT_PARAMETER);
	}

	private TokenHandlerFactory createTokenHandlerFactory(ServletContext context) {

		TokenHandlerFactory factory = new TokenHandlerSpringFactory(context);

		// Replace with this factory impl if you are not using Spring
		// TokenHandlerFactory factory = new TokenHandlerStandaloneFactory();

		return factory;
	}

	private void redirectToLogin(ServletResponse response) throws IOException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.sendRedirect(urlLogin);
	}

}
