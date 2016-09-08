package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Div;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.model.DomainView;
import com.qsocialnow.model.Thematic;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.ThematicService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class EditDomainViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private DomainService domainService;

    @WireVariable("mockThematicService")
    private ThematicService thematicService;

    private DomainView currentDomain;

    private List<Thematic> thematics;

    private boolean saved;

    public boolean isSaved() {
        return saved;
    }

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
        currentDomain = new DomainView();
        currentDomain.setDomain(domainService.findOne(domain));
        thematics = thematicService.findAll();
        currentDomain.setSelectedThematics(thematics.stream()
                .filter(thematic -> currentDomain.getDomain().getThematics().contains(thematic.getId()))
                .collect(Collectors.toSet()));
    }

    @Command
    @NotifyChange({ "currentDomain", "saved" })
    public void save() {
        Domain domain = currentDomain.getDomain();
        domain.setThematics(currentDomain.getSelectedThematics().stream().map(Thematic::getId)
                .collect(Collectors.toList()));
        currentDomain.setDomain(domainService.update(domain));
        initThematics();
        Clients.showNotification(Labels.getLabel("domain.edit.notification.success", new String[] { currentDomain
                .getDomain().getId() }));
        saved = true;
    }

    @Command
    @NotifyChange("currentDomain")
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
        if (saved) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("domainChanged", currentDomain.getDomain());
            BindUtils.postGlobalCommand(null, null, "changeDomain", args);
        }
    }

}
