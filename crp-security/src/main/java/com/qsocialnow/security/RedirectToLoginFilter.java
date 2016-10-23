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

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.qsocialnow.security.exception.AuthorizationException;

public class RedirectToLoginFilter implements Filter {

	private static final Logger LOGGER = Logger.getLogger(RedirectToLoginFilter.class);

	@Override
	public void destroy() {
		LOGGER.info("=== Destroying RedirectToLoginFilter ===");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (AuthorizationException e) {
			redirectToLogin(request, response);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		LOGGER.info("=== Initializing RedirectToLoginFilter ===");
	}

	private void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		ServletContext context = request.getServletContext();
		WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		LoginConfig loginConf = springContext.getBean(LoginConfig.class);
		String urlLogin = loginConf.getLoginUrl();

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.sendRedirect(urlLogin);
	}

}
