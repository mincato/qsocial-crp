package com.qsocialnow.security;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.qsocialnow.security.exception.SessionExpiredException;
import com.qsocialnow.security.exception.ShortTokenExpiredException;
import com.qsocialnow.security.exception.TokenNotFoundException;

public class AuthorizationHelper {

	private static final String TOKEN_REQUEST_PARAMETER = "token";
	private static final String TOKEN_SESSION_PARAMETER = "token";
	private static final String SHORT_TOKEN_SESSION_PARAMETER = "shortToken";

	public static final String USER_SESSION_PARAMETER = "user";

	public boolean saveUserInSession(ServletRequest request) {

		try {
			ServletContext context = request.getServletContext();
			TokenHandlerFactory factory = createTokenHandlerFactory(context);
			TokenHandler tokenHandler = factory.create();
			ZookeeperClient zookeeperClient = new ZookeeperClient(context);
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpSession session = httpRequest.getSession(true);

			// Se busca si hay un shortToken en el request parameter
			String token = findTokenFromRequestParam(zookeeperClient, httpRequest);

			// Si no se recibio un shortToken, no se hace nada
			if (StringUtils.isBlank(token)) {
				return false;
			}

			UserData user = tokenHandler.verifyToken(token);

			UserActivityData activity = zookeeperClient.findUserActivityData(token);
			verifyExpiration(token, activity, zookeeperClient);

			session.setMaxInactiveInterval((int) activity.getSessionTimeoutInSeconds());
			session.setAttribute(TOKEN_SESSION_PARAMETER, token);
			session.setAttribute(USER_SESSION_PARAMETER, user);
			return true;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public void authorize(ServletRequest request) {

		try {
			ServletContext context = request.getServletContext();
			ZookeeperClient zookeeperClient = new ZookeeperClient(context);
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpSession session = httpRequest.getSession(true);

			// El token tiene que estar si o si en la sesion
			String token = (String) session.getAttribute(TOKEN_SESSION_PARAMETER);

			if (StringUtils.isBlank(token)) {
				throw new TokenNotFoundException();
			}

			UserActivityData activity = zookeeperClient.findUserActivityData(token);
			verifyExpiration(token, activity, zookeeperClient);

			// Se actualiza la actividad en zookeeper
			zookeeperClient.updateUserActivityData(token, activity);
			
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	private String findTokenFromRequestParam(ZookeeperClient zookeeperClient, HttpServletRequest httpRequest)
			throws Exception {

		String token = null;
		String shortToken = httpRequest.getParameter(TOKEN_REQUEST_PARAMETER);
		if (StringUtils.isNotBlank(shortToken)) {

			// Se busca si el short token ya fue usado y si esta en la sesion,
			// para no ir a zookeeper
			HttpSession session = httpRequest.getSession();
			String shortTokenFromSession = (String) session.getAttribute(SHORT_TOKEN_SESSION_PARAMETER);
			if (shortToken.equals(shortTokenFromSession)) {
				return (String) session.getAttribute(TOKEN_SESSION_PARAMETER);
			}

			// Si es un short token nuevo hay que ir a buscarlo a zookeeper
			ShortTokenEntry entry = zookeeperClient.findToken(shortToken);

			// El short token se remueve de zookeeper para evitar accesos
			// foraneos
			zookeeperClient.removeToken(shortToken);

			if (entry.isExpired()) {
				throw new ShortTokenExpiredException();
			}
			token = entry.getToken();

			session.setAttribute(SHORT_TOKEN_SESSION_PARAMETER, shortToken);
		}
		return token;
	}

	private void verifyExpiration(String token, UserActivityData activity, ZookeeperClient zookeeperClient)
			throws Exception {

		if (activity.isExpired()) {
			// Si la sesion esta expirada se borra de zookeeper
			zookeeperClient.removeSession(token);
			throw new SessionExpiredException();
		}
	}

	private TokenHandlerFactory createTokenHandlerFactory(ServletContext context) {

		TokenHandlerFactory factory = new TokenHandlerSpringFactory(context);

		// Replace with this factory impl if you are not using Spring
		// TokenHandlerFactory factory = new TokenHandlerStandaloneFactory();

		return factory;
	}

}
