package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.model.DomainView;
import com.qsocialnow.model.Thematic;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.ThematicService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateDomainViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private DomainService domainService;

    @WireVariable("mockThematicService")
    private ThematicService thematicService;

    private DomainView currentDomain;

    private List<Thematic> thematics;

    public DomainView getCurrentDomain() {
        return currentDomain;
    }

    public List<Thematic> getThematics() {
        return thematics;
    }

    @Init
    public void init() {
        initDomain();
        thematics = thematicService.findAll();
    }

    private void initDomain() {
        currentDomain = new DomainView();
        currentDomain.setDomain(new Domain());
        currentDomain.setSelectedThematics(new HashSet<>());
        currentDomain.setResolutions(new ArrayList<Resolution>());
    }

    @Command
    @NotifyChange("currentDomain")
    public void save() {
        List<Long> thematics = currentDomain.getSelectedThematics().stream().map(Thematic::getId)
                .collect(Collectors.toList());
        Domain newDomain = currentDomain.getDomain();
        newDomain.setThematics(thematics);
        newDomain.setResolutions(currentDomain.getResolutions());
        currentDomain.setDomain(domainService.create(newDomain));
        Clients.showNotification(Labels.getLabel("domain.create.notification.success", new String[] { currentDomain
                .getDomain().getName() }));
        initDomain();
    }

    @Command
    @NotifyChange({ "currentDomain" })
    public void clear() {
        initDomain();
    }

    @Command
    public void addResolution(@BindingParam("domain") DomainView domain) {
        domain.getResolutions().add(new Resolution());
        BindUtils.postNotifyChange(null, null, domain, "resolutions");
    }

    @Command
    public void deleteResolution(@BindingParam("index") int idx, @BindingParam("domain") DomainView domain) {
        domain.getResolutions().remove(idx);
        BindUtils.postNotifyChange(null, null, domain, "resolutions");
    }

}