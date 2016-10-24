package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.ThematicService;

@VariableResolver(DelegatingVariableResolver.class)
public class DomainsViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    private int pageSize = 15;
    private int activePage = 0;

    @WireVariable
    private DomainService domainService;

    private boolean moreResults;

    private List<DomainListView> domains = new ArrayList<>();

    private String keyword;

    @WireVariable
    private ThematicService thematicService;

    private Map<Long, Thematic> thematicsById;

    @Init
    public void init() {
        findDomains();
        findThematics();
        fillThematicsInDomains();
    }

    public List<DomainListView> getDomains() {
        return this.domains;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public boolean isMoreResults() {
        return moreResults;
    }

    @Command
    @NotifyChange({ "domains", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findDomains();
    }

    @Command
    @NotifyChange({ "domains", "moreResults" })
    public void search() {
        this.findDomainsByName();
    }

    @Command
    public void openEdit(@BindingParam("domainId") String domainId) {
        Map<String, Object> arg = new HashMap<String, Object>();
        arg.put("domain", domainId);
        Executions.createComponents("/pages/domain/edit-domain.zul", null, arg);
    }

    @GlobalCommand
    @NotifyChange("domains")
    public void changeDomain(@BindingParam("domainChanged") Domain domainChanged) {
        if (domainChanged != null) {
            Optional<DomainListView> domainOptional = domains.stream()
                    .filter(domain -> domain.getId().equals(domainChanged.getId())).findFirst();
            if (domainOptional.isPresent()) {
                DomainListView domainListView = domainOptional.get();
                domainListView.setName(domainChanged.getName());
                domainListView.setThematics(findThematicsByDomain(domainChanged));
            }
        }
    }

    private Collection<Thematic> findThematicsByDomain(Domain domain) {
        Collection<Thematic> thematicsByDomain = new ArrayList<>();
        for (Long thematicId : domain.getThematics()) {
            Thematic thematic = thematicsById.get(thematicId);
            thematicsByDomain.add(thematic);
        }
        return thematicsByDomain;
    }

    private PageResponse<DomainListView> findDomainsByName() {
        PageResponse<DomainListView> pageResponse = domainService.findAllByName(activePage, pageSize, this.keyword);
        this.domains.clear();
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.domains.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    private PageResponse<DomainListView> findDomains() {
        PageResponse<DomainListView> pageResponse = domainService.findAll(activePage, pageSize);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.domains.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }

        return pageResponse;
    }

    private void findThematics() {
        List<Thematic> thematics = thematicService.findAll();
        thematicsById = new HashMap<>();
        for (Thematic thematic : thematics) {
            thematicsById.put(thematic.getId(), thematic);
        }
    }

    private void fillThematicsInDomains() {
        for (DomainListView domain : domains) {
            List<Long> ids = domain.getThematicIds();
            Collection<Thematic> thematics = new ArrayList<>();
            for (Long id : ids) {
                Thematic thematic = thematicsById.get(id);
                if (thematic != null) {
                    thematics.add(thematicsById.get(id));
                }
            }
            domain.setThematics(thematics);
        }
    }

}
