package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Div;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.model.DomainView;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.ThematicService;

@VariableResolver(DelegatingVariableResolver.class)
public class EditDomainViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private DomainService domainService;

    @WireVariable
    private ThematicService thematicService;

    private DomainView currentDomain = new DomainView();

    private List<Thematic> thematics;

    public DomainView getCurrentDomain() {
        return currentDomain;
    }

    public List<Thematic> getThematics() {
        return thematics;
    }

    public void setCurrentDomain(DomainView currentDomain) {
        this.currentDomain = currentDomain;
    }

    @Init
    public void init(@BindingParam("domain") String domain) {
        currentDomain.setDomain(domainService.findOne(domain));
        thematics = thematicService.findAll();
        initThematics();
    }

    @Command
    @NotifyChange({ "currentDomain" })
    public void save() {
        Domain domain = currentDomain.getDomain();
        domain.setThematics(currentDomain.getSelectedThematics().stream().map(Thematic::getId)
                .collect(Collectors.toList()));
        currentDomain.setDomain(domainService.update(domain));
        initThematics();
        Clients.showNotification(Labels.getLabel("domain.edit.notification.success", new String[] { currentDomain
                .getDomain().getId() }));
    }

    @Command
    @NotifyChange({ "currentDomain", "currentDomain.selectedThematics" })
    public void clear() {
        initThematics();
    }

    private void initThematics() {
        currentDomain.getSelectedThematics().clear();
        currentDomain.getSelectedThematics().addAll(
                thematics.stream()
                        .filter(thematic -> currentDomain.getDomain().getThematics().contains(thematic.getId()))
                        .collect(Collectors.toSet()));
    }

    @Command
    public void close(@ContextParam(ContextType.VIEW) Div comp) {
        comp.detach();
    }

}
