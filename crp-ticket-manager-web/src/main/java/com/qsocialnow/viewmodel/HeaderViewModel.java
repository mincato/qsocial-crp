package com.qsocialnow.viewmodel;

import java.io.Serializable;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.services.impl.AnalyticsAuthService;

@VariableResolver(DelegatingVariableResolver.class)
public class HeaderViewModel implements Serializable {

    private static final long serialVersionUID = -5168365811321248975L;

    @WireVariable
    private AnalyticsAuthService analyticsAuthService;

    @Init
    public void init() {
    }

    @Command
    public void redirectAnalytics() {
        analyticsAuthService.redirectAnalyticsHome();
    }

}
