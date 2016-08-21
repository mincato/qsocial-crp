package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.ThematicService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateDomainViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private DomainService domainService;

    @WireVariable
    private ThematicService thematicService;

    private Domain currentDomain;

    private List<Thematic> thematics;

    private Set<Thematic> selectedThematics;

    public Domain getCurrentDomain() {
        return currentDomain;
    }

    public List<Thematic> getThematics() {
        return thematics;
    }

    public Set<Thematic> getSelectedThematics() {
        return selectedThematics;
    }

    public void setSelectedThematics(Set<Thematic> selectedThematics) {
        this.selectedThematics = selectedThematics;
    }

    @Init
    public void init() {
        currentDomain = new Domain();
        thematics = thematicService.findAll();
        selectedThematics = new HashSet<>();

    }

    @Command
    @NotifyChange("currentDomain")
    public void save() {
        List<Long> thematics = selectedThematics.stream().map(Thematic::getId).collect(Collectors.toList());
        currentDomain.setThematics(thematics);
        currentDomain = domainService.create(currentDomain);
        Clients.showNotification(Labels.getLabel("domain.create.notification.success",
                new String[] { currentDomain.getId() }));
    }

    @Command
    public void openEdit() {
        Map<String, Object> arg = new HashMap<String, Object>();
        arg.put("domain", "ramon");
        Executions.createComponents("/pages/domain/edit-domain.zul", null, arg);
    }

    @Command
    @NotifyChange({ "currentDomain", "selectedThematics" })
    public void clear() {
        selectedThematics.clear();
    }

}
