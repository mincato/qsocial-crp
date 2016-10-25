package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Filedownload;

import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.ResultsService;

@VariableResolver(DelegatingVariableResolver.class)
public class ResultsViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    private static final int PAGE_SIZE_DEFAULT = 15;

    private static final int ACTIVE_PAGE_DEFAULT = 0;

    private int pageSize = PAGE_SIZE_DEFAULT;

    private int activePage = ACTIVE_PAGE_DEFAULT;

    @WireVariable
    private ResultsService resultsService;

    @WireVariable
    private DomainService domainService;

    private boolean moreResults;

    private List<ResultsListView> results = new ArrayList<>();

    // filters
    private boolean filterActive;

    private List<DomainListView> domains = new ArrayList<>();

    private DomainListView domain;

    @Init
    public void init() {
        this.filterActive = false;
        this.domains = getDomainsList();
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public boolean isMoreResults() {
        return moreResults;
    }

    @Command
    @NotifyChange({ "results", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.sumarizeCases();
    }

    private PageResponse<ResultsListView> sumarizeCases() {
        PageRequest pageRequest = new PageRequest(activePage, pageSize, "");
        pageRequest.setSortOrder(true);

        PageResponse<ResultsListView> pageResponse = resultsService.sumarizeAll(pageRequest,
                filterActive ? getFilters() : null);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.results.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    @Command
    @NotifyChange({ "results", "moreResults", "filterActive" })
    public void search() {
        this.filterActive = true;
        this.setDefaultPage();
        this.results.clear();
        this.sumarizeCases();
    }

    @Command
    public void download() {
        byte[] data = resultsService.getReport(getFilters());
        Filedownload.save(data, "application/vnd.ms-excel", "file.xls");
    }

    private List<DomainListView> getDomainsList() {
        List<DomainListView> domains = new ArrayList<>();
        PageResponse<DomainListView> domainsResponse = domainService.findAll(0, -1);
        domains = domainsResponse.getItems();
        return domains;
    }

    private Map<String, String> getFilters() {
        Map<String, String> filters = new HashMap<String, String>();
        if (domain != null) {
            filters.put("domainId", domain.getId());
        }
        return filters;
    }

    public List<ResultsListView> getResults() {
        return results;
    }

    public void setResults(List<ResultsListView> results) {
        this.results = results;
    }

    private void setDefaultPage() {
        this.pageSize = PAGE_SIZE_DEFAULT;
        this.activePage = ACTIVE_PAGE_DEFAULT;
    }

    public boolean isFilterActive() {
        return filterActive;
    }

    public void setFilterActive(boolean filterActive) {
        this.filterActive = filterActive;
    }

    public List<DomainListView> getDomains() {
        return domains;
    }

    public void setDomains(List<DomainListView> domains) {
        this.domains = domains;
    }

    public DomainListView getDomain() {
        return domain;
    }

    public void setDomain(DomainListView domain) {
        this.domain = domain;
    }
}
