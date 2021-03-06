package com.qsocialnow.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.curator.framework.CuratorFramework;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Deprecated
public class AuthorizationFilter implements Filter {

	private static final Logger LOGGER = Logger.getLogger(AuthorizationFilter.class);

	private static final String URL_LOGIN_INIT_PARAMETER = "urlLogin";

	private String urlLogin;

	@Override
	public void destroy() {
		LOGGER.info("=== Destroying AuthorizationFilter ===");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		boolean continueProcessing = true;
		
		try {
			ServletContext context = request.getServletContext();
			WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
			CuratorFramework curatorFramework = (CuratorFramework) springContext.getBean("zookeeperClient");
			
			AuthorizationHelper authHelper = new AuthorizationHelper();
			authHelper.saveUserInSession(request, curatorFramework);
			authHelper.authorize(request);
			
		} catch (Exception e) {
			LOGGER.error("Error authorizing token", e);
			redirectToLogin(response);
			continueProcessing = false;
		}
		if (continueProcessing) {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		LOGGER.info("=== Initializing AuthorizationFilter ===");

		urlLogin = config.getInitParameter(URL_LOGIN_INIT_PARAMETER);
	}

	private void redirectToLogin(ServletResponse response) throws IOException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.sendRedirect(urlLogin);
	}

}
