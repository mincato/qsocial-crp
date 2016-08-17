package com.qsocialnow.viewmodel;

import java.io.Serializable;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.services.DomainService;

@VariableResolver(DelegatingVariableResolver.class)
public class DomainViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private DomainService domainService;

    private Domain currentDomain;

    public Domain getCurrentDomain() {
        return currentDomain;
    }

    @Init
    public void init() {
        currentDomain = new Domain();
    }

    @Command
    @NotifyChange("currentDomain")
    public void save() {
        currentDomain = domainService.create(currentDomain);
        Clients.showNotification(Labels.getLabel("domain.create.notification.success",
                new String[] { currentDomain.getId() }));
    }

    @Command
    @NotifyChange("currentDomain")
    public void clear() {
    }

}
