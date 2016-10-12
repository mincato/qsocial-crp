package com.qsocialnow.security;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class TokenHandlerSpringFactory implements TokenHandlerFactory {
	
	private ServletContext context;
	
	public TokenHandlerSpringFactory(ServletContext context) {
		this.context = context;
	}

	@Override
	public TokenHandler create() {
		WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		TokenHandler tokenHandler = springContext.getBean(TokenHandler.class);
		return tokenHandler;
	}

}
