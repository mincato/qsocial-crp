package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Filedownload;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.cases.CasesFilterRequestReport;
import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.ResultsService;
import com.qsocialnow.services.UserSessionService;

import javassist.bytecode.annotation.BooleanMemberValue;

@VariableResolver(DelegatingVariableResolver.class)
public class ResultsViewModel implements Serializable {

    private static final String REPORT_OPTION_STATE = "state";

    private static final String REPORT_OPTION_ADMIN = "admin";

    private static final String REPORT_OPTION_RESOLUTION = "resolution";

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

    private String currentResolution;

    private List<ResultsListView> resultsByUser = new ArrayList<>();

    // filters
    private boolean filterActive;

    private List<DomainListView> domains = new ArrayList<>();

    private DomainListView domain;

    private List<String> resultsTypeOptions = new ArrayList<>();

    private String reportType;

    private boolean byResolution;

    private boolean byAdmin;

    private boolean byState;

    @WireVariable
    private UserSessionService userSessionService;

    @Init
    public void init() {
        this.filterActive = false;
        this.resultsTypeOptions = getResultsOptionsList();
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
        CasesFilterRequest filterRequest = new CasesFilterRequest();
        PageRequest pageRequest = new PageRequest(activePage, pageSize, "");
        filterRequest.setPageRequest(pageRequest);
        filterRequest.setFilterActive(filterActive);
        setFilters(filterRequest);
        PageResponse<ResultsListView> pageResponse = resultsService.sumarizeAll(filterRequest);

        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.results.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    private PageResponse<ResultsListView> sumarizeResolutionsByUser(String idResolution) {
        CasesFilterRequest filterRequest = new CasesFilterRequest();
        PageRequest pageRequest = new PageRequest(activePage, pageSize, "");
        filterRequest.setPageRequest(pageRequest);
        filterRequest.setFilterActive(filterActive);
        setFilters(filterRequest);
        filterRequest.setIdResolution(idResolution);

        PageResponse<ResultsListView> pageResponse = resultsService.sumarizeResolutionByUser(filterRequest);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.resultsByUser.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    @Command
    @NotifyChange({ "results", "moreResults", "filterActive", "resultsByUser", "currentResolution" })
    public void search() {
        this.filterActive = true;
        this.setDefaultPage();
        this.results.clear();
        this.resultsByUser.clear();
        this.currentResolution = "";
        this.sumarizeCases();
    }

    @Command
    public void download() {
        CasesFilterRequest filterRequest = new CasesFilterRequest();
        filterRequest.setFilterActive(filterActive);
        setFilters(filterRequest);
        CasesFilterRequestReport filterRequestReport = new CasesFilterRequestReport();
        filterRequestReport.setFilterRequest(filterRequest);
        filterRequestReport.setLanguage(userSessionService.getLanguage());
        byte[] data = resultsService.getReport(filterRequestReport);
        Filedownload.save(data, "application/vnd.ms-excel", "file.xls");
    }

    @Command
    @NotifyChange({ "currentResolution", "resultsByUser", "filterActive" })
    public void searchResolutionByUser(@BindingParam("idResolution") String idResolution,
            @BindingParam("resolution") String resolution) {
        this.filterActive = true;
        this.resultsByUser.clear();
        this.setDefaultPage();
        this.sumarizeResolutionsByUser(idResolution);
        this.currentResolution = resolution;
    }

    @Command
    @NotifyChange({ "byResolution", "byAdmin", "byState" })
    public void showOption() {
        switch (this.reportType) {
            case REPORT_OPTION_RESOLUTION:
                this.byResolution = true;
                this.byAdmin = false;
                this.byState = false;
                break;

            case REPORT_OPTION_STATE:
                this.byResolution = false;
                this.byAdmin = false;
                this.byState = true;
                break;

            default:
                break;
        }
    }

    private List<DomainListView> getDomainsList() {
        List<DomainListView> domains = new ArrayList<>();
        PageResponse<DomainListView> domainsResponse = domainService.findAll();
        domains = domainsResponse.getItems();
        return domains;
    }

    private void setFilters(CasesFilterRequest filterRequest) {
        if (domain != null) {
            filterRequest.setDomain(domain.getId());
        }
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

    private List<String> getResultsOptionsList() {
        String[] options = { REPORT_OPTION_RESOLUTION, REPORT_OPTION_STATE };
        return new ArrayList<String>(Arrays.asList(options));
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

    public List<String> getResultsTypeOptions() {
        return resultsTypeOptions;
    }

    public void setResultsTypeOptions(List<String> resultsTypeOptions) {
        this.resultsTypeOptions = resultsTypeOptions;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public boolean isByResolution() {
        return byResolution;
    }

    public void setByResolution(boolean byResolution) {
        this.byResolution = byResolution;
    }

    public boolean isByAdmin() {
        return byAdmin;
    }

    public void setByAdmin(boolean byAdmin) {
        this.byAdmin = byAdmin;
    }

    public boolean isByState() {
        return byState;
    }

    public void setByState(boolean byState) {
        this.byState = byState;
    }

    public List<ResultsListView> getResultsByUser() {
        return resultsByUser;
    }

    public void setResultsByUser(List<ResultsListView> resultsByUser) {
        this.resultsByUser = resultsByUser;
    }

    public String getCurrentResolution() {
        return currentResolution;
    }

    public void setCurrentResolution(String currentResolution) {
        this.currentResolution = currentResolution;
    }
}
