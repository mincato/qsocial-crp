package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.services.SourceService;

@VariableResolver(DelegatingVariableResolver.class)
public class AdminViewModel implements Serializable {

    private static final long serialVersionUID = -8003220629324787312L;

    @WireVariable
    private SourceService sourceService;

    @Init
    public void init() {
        Set<Long> blockedSources = sourceService.getBlockedSources();
        if (CollectionUtils.isNotEmpty(blockedSources)) {
            Map<String, Object> params = new HashMap<>();
            params.put("blockedSources", blockedSources);
            Executions.createComponents("/pages/admin/blocked-sources-message.zul", null, params);
        }
    }
}
