package com.qsocialnow.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.GenericInitiator;

import com.qsocialnow.security.AuthorizationHelper;

public class AjaxAccessDeniedHandler extends GenericInitiator {

	public void doInit(Page page, Map<String, Object> args) throws Exception {

		try {
			Execution exec = Executions.getCurrent();
			HttpServletRequest request = (HttpServletRequest) exec.getNativeRequest();

			AuthorizationHelper authHelper = new AuthorizationHelper();
			authHelper.authorize(request);

		} catch (Exception e) {
			redirectToLogin();
		}
	}

	private void redirectToLogin() throws Exception {
		Execution exec = Executions.getCurrent();
		HttpServletResponse response = (HttpServletResponse) exec.getNativeResponse();
		response.sendRedirect(response.encodeRedirectURL("http://localhost:8080/crp-login-web/"));
		exec.setVoided(true);
	}

}
