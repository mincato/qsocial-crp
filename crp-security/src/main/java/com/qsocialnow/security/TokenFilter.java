package com.qsocialnow.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class TokenFilter implements Filter {

	private static final Logger LOGGER = Logger.getLogger(TokenFilter.class);

	private static final String URL_LOGIN_INIT_PARAMETER = "urlLogin";
	private static final String TOKEN_PARAMETER = "token";
	private static final String TOKEN_SESSION_PARAMETER = "token";

	private String urlLogin;

	@Override
	public void destroy() {
		LOGGER.info("=== Destroying TokenFiler ===");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		LOGGER.info("=== TokenFilter ===");

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String token = httpRequest.getParameter(TOKEN_PARAMETER);
		
		HttpSession session = httpRequest.getSession(true);
		
		if (StringUtils.isBlank(token)) {
			token = (String) session.getAttribute(TOKEN_SESSION_PARAMETER);
		}
		
		if (StringUtils.isNotBlank(token)) {
			
			session.setAttribute(TOKEN_SESSION_PARAMETER, token);

			chain.doFilter(request, response);
		} else {
			
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendRedirect(urlLogin);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		LOGGER.info("=== Initializing TokenFiler ===");

		urlLogin = config.getInitParameter(URL_LOGIN_INIT_PARAMETER);
	}

}
