package com.qsocialnow.handler;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.GenericInitiator;

import com.qsocialnow.security.AuthorizationHelper;
import com.qsocialnow.security.LoginConfigBean;

public class AjaxAccessDeniedHandler extends GenericInitiator {

	public void doInit(Page page, Map<String, Object> args) throws Exception {

		Execution exec = Executions.getCurrent();
		HttpServletRequest request = (HttpServletRequest) exec.getNativeRequest();		
		
		try {
			AuthorizationHelper authHelper = new AuthorizationHelper();
			authHelper.authorize(request);

		} catch (Exception e) {
			String loginUrl = findLoginUrl(request);
			redirectToLogin(loginUrl);
		}
	}

	private void redirectToLogin(String loginUrl) throws Exception {
		Execution exec = Executions.getCurrent();
		HttpServletResponse response = (HttpServletResponse) exec.getNativeResponse();
		response.sendRedirect(response.encodeRedirectURL(loginUrl));
		exec.setVoided(true);
	}
	
	private String findLoginUrl(HttpServletRequest httpRequest) {
		ServletContext context = httpRequest.getServletContext();
		WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		
		LoginConfigBean loginConfigBean = springContext.getBean(LoginConfigBean.class);
		String loginUrl = loginConfigBean.getLoginUrl();
		return loginUrl;
	}

}
