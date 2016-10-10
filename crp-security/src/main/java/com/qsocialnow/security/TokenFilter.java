package com.qsocialnow.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

public class TokenFilter implements Filter {
	
	private static final Logger LOGGER = Logger.getLogger(TokenFilter.class);

	@Override
	public void destroy() {
		LOGGER.info("=== Destroying TokenFiler ===");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		LOGGER.info("=== TokenFilter ===");
		
		chain.doFilter(request, response);		
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		LOGGER.info("=== Initializing TokenFiler ===");
	}

}
