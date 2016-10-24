package com.qsocialnow.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.GenericInitiator;

import com.qsocialnow.security.AuthorizationHelper;
import com.qsocialnow.security.exception.AuthorizationException;

public class AjaxAccessDeniedHandler extends GenericInitiator {

	public void doInit(Page page, Map<String, Object> args) throws Exception {

		Execution exec = Executions.getCurrent();
		HttpServletRequest request = (HttpServletRequest) exec.getNativeRequest();

		try {
			AuthorizationHelper authHelper = new AuthorizationHelper();
			authHelper.authorize(request);

		} catch (Exception e) {
			throw new AuthorizationException();
		}
	}

}
