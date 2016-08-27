package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.DomainService;

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

    @Init
    public void init() {
        findDomains();
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

}
