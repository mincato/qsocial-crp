package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.ThematicService;

@VariableResolver(DelegatingVariableResolver.class)
public class EditDomainViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private DomainService domainService;

    @WireVariable
    private ThematicService thematicService;

    private Domain currentDomain;

    private List<Thematic> thematicsOptions;

    private Set<Thematic> selectedThematics = new HashSet<>();

    public Domain getCurrentDomain() {
        return currentDomain;
    }

    public List<Thematic> getThematicsOptions() {
        return thematicsOptions;
    }

    public Set<Thematic> getSelectedThematics() {
        return selectedThematics;
    }

    public void setSelectedThematics(Set<Thematic> selectedThematics) {
        this.selectedThematics = selectedThematics;
    }

    @Init
    public void init(@BindingParam("domain") String domain) {
        currentDomain = domainService.findOne(domain);
        thematicsOptions = thematicService.findAll();
        initThematics();
    }

    @Command
    @NotifyChange({ "currentDomain", "selectThematics" })
    public void save() {
        currentDomain.setThematics(selectedThematics.stream().map(Thematic::getId).collect(Collectors.toList()));
        currentDomain = domainService.update(currentDomain);
        initThematics();
        Clients.showNotification(Labels.getLabel("domain.create.notification.success",
                new String[] { currentDomain.getId() }));
    }

    @Command
    @NotifyChange({ "currentDomain", "selectedThematics" })
    public void clear() {
        initThematics();
    }

    private void initThematics() {
        selectedThematics.clear();
        selectedThematics.addAll(thematicsOptions.stream()
                .filter(thematic -> currentDomain.getThematics().contains(thematic.getId()))
                .collect(Collectors.toSet()));
    }

    @Command
    public void close(@ContextParam(ContextType.VIEW) AbstractComponent comp) {
        comp.detach();
    }

}
