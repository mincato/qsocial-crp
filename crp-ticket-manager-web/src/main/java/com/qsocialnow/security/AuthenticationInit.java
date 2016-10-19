package com.qsocialnow.security;

import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

public class AuthenticationInit implements Initiator {

    @Override
    public void doInit(Page page, Map<String, Object> args) throws Exception {

        String authorities = (String) args.get("roles");
        if (!SecurityUtil.isAllGranted(authorities)) {
            Executions.sendRedirect("/pages/errors/forbiddenError.zul");
        }

    }

}
