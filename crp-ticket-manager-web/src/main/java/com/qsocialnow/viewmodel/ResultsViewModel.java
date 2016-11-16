package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Filedownload;

import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.cases.CasesFilterRequestReport;
import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.SegmentListView;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.util.UserConstants;
import com.qsocialnow.converters.DateConverter;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.ResultsService;
import com.qsocialnow.services.TriggerService;
import com.qsocialnow.services.UserService;
import com.qsocialnow.services.UserSessionService;

@NotifyCommand(value = "zmapbox$clientUpdate", onChange = "_vm_.geoJson")
@ToClientCommand({ "zmapbox$clientUpdate" })
@VariableResolver(DelegatingVariableResolver.class)
public class ResultsViewModel implements Serializable {

    private static final String REPORT_OPTION_STATE = "state";

    private static final String REPORT_OPTION_ADMIN = "admin";

    private static final String REPORT_OPTION_RESOLUTION = "resolution";

    private static final String REPORT_OPTION_MAP = "map";

    private static final long serialVersionUID = 2259179419421396093L;

    private static final int PAGE_SIZE_DEFAULT = 15;

    private static final int ACTIVE_PAGE_DEFAULT = 0;

    private int pageSize = PAGE_SIZE_DEFAULT;

    private int activePage = ACTIVE_PAGE_DEFAULT;

    private static final String NOT_ID_ITEM_VALUE = "NOT_ID";

    private static final Integer NOT_ID_INT_VALUE = -1;

    private DateConverter dateConverter;

    private static final String CASES_ALL_LABEL_KEY = "cases.list.filter.all";

    private static final String FALSE_OPTION_VALUE = "false";

    private static final String TRUE_OPTION_VALUE = "true";

    private static final String ALL_OPTION_VALUE = "all";

    private static final String HIGH_OPTION_VALUE = "HIGH";

    private static final String MEDIUM_OPTION_VALUE = "MEDIUM";

    private static final String LOW_OPTION_VALUE = "LOW";

    @WireVariable
    private ResultsService resultsService;

    @WireVariable
    private DomainService domainService;

    private boolean moreResults;

    private List<ResultsListView> results = new ArrayList<>();

    private String currentResolution;

    private List<ResultsListView> resultsByUser = new ArrayList<>();

    private List<ResultsListView> resultsState = new ArrayList<>();

    private List<ResultsListView> adminUnits = new ArrayList<>();

    private List<ResultsListView> statusByUser = new ArrayList<>();

    private List<ResultsListView> statusByPending = new ArrayList<>();

    private String currentStatus;

    // filters
    private boolean filterActive;

    private boolean showFilters;

    private List<DomainListView> domains = new ArrayList<>();

    private DomainListView domain;

    private List<TriggerListView> triggers = new ArrayList<>();

    private TriggerListView trigger;

    private List<SegmentListView> segments = new ArrayList<>();

    private SegmentListView segment;

    private List<UserListView> users = new ArrayList<>();

    private UserListView userSelected;

    private List<String> pendingOptions = new ArrayList<>();

    private List<String> statusOptions = new ArrayList<>();

    private List<String> priorityOptions = new ArrayList<>();

    private List<String> adminUnitsOptions = new ArrayList<>();

    private List<String> resultsTypeOptions = new ArrayList<>();

    private Long fromDate;

    private Long toDate;

    private String title;

    private String subject;

    private String userName;

    private String status;

    private String priority;

    private String pendingResponse;

    private String adminUnit;

    private String reportType;

    private boolean byResolution;

    private boolean byAdmin;

    private boolean byState;

    private boolean byMap;

    @WireVariable
    private UserSessionService userSessionService;

    @WireVariable
    private TriggerService triggerService;

    @WireVariable
    private UserService userService;

    @WireVariable
    private CaseService caseService;

    private String geoJson;

    @Init
    public void init() {
        this.filterActive = false;
        this.showFilters = false;
        this.resultsTypeOptions = getResultsOptionsList();
        this.domains = getDomainsList();
        this.users = getUsersList();
        this.pendingOptions = getPendingOptionsList();
        this.statusOptions = getStatusOptionsList();
        this.priorityOptions = getPriorityOptionsList();
        this.adminUnitsOptions = getAdminUnitsOptionsList();
        this.dateConverter = new DateConverter(userSessionService.getTimeZone());
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
        filterRequest.setFieldToSumarize(UserConstants.REPORT_BY_RESOLUTION);
        PageResponse<ResultsListView> pageResponse = resultsService.sumarizeAll(filterRequest);

        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.results.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    private PageResponse<ResultsListView> sumarizeCasesByStatus() {
        CasesFilterRequest filterRequest = new CasesFilterRequest();
        PageRequest pageRequest = new PageRequest(activePage, pageSize, "");
        filterRequest.setPageRequest(pageRequest);
        filterRequest.setFilterActive(filterActive);
        setFilters(filterRequest);
        filterRequest.setFieldToSumarize(UserConstants.REPORT_BY_STATUS);
        PageResponse<ResultsListView> pageResponse = resultsService.sumarizeAll(filterRequest);

        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.resultsState.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    private PageResponse<ResultsListView> sumarizeCasesByUnitAdmin() {
        CasesFilterRequest filterRequest = new CasesFilterRequest();
        PageRequest pageRequest = new PageRequest(activePage, 10, "");
        filterRequest.setPageRequest(pageRequest);
        filterRequest.setFilterActive(filterActive);
        setFilters(filterRequest);
        filterRequest.setFieldToSumarize(adminUnit);
        PageResponse<ResultsListView> pageResponse = resultsService.sumarizeAll(filterRequest);

        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.adminUnits.addAll(pageResponse.getItems());
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

    private PageResponse<ResultsListView> sumarizeStatusByUser(String status) {
        CasesFilterRequest filterRequest = new CasesFilterRequest();
        PageRequest pageRequest = new PageRequest(activePage, pageSize, "");
        filterRequest.setPageRequest(pageRequest);
        filterRequest.setFilterActive(filterActive);
        setFilters(filterRequest);
        filterRequest.setStatus(status);

        PageResponse<ResultsListView> pageResponse = resultsService.sumarizeStatusByUser(filterRequest);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.statusByUser.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    @Command
    @NotifyChange({ "results", "moreResults", "filterActive", "resultsByUser", "currentResolution", "resultsState",
            "statusByUser", "adminUnits" })
    public void search() {
        this.setDefaultPage();
        this.filterActive = true;
        if (byResolution) {
            this.results.clear();
            this.resultsByUser.clear();
            this.currentResolution = "";
            this.sumarizeCases();
        } else if (byState) {
            this.resultsState.clear();
            this.statusByUser.clear();
            this.currentStatus = "";
            this.sumarizeCasesByStatus();
        } else if (byAdmin) {
            this.adminUnits.clear();
            this.sumarizeCasesByUnitAdmin();
        }
    }

    @Command
    public void download() {
        CasesFilterRequest filterRequest = new CasesFilterRequest();
        filterRequest.setFilterActive(filterActive);
        setFilters(filterRequest);
        if (byResolution) {
        	 filterRequest.setFieldToSumarize(UserConstants.REPORT_BY_RESOLUTION);
        } else if (byState) {
        	filterRequest.setFieldToSumarize(UserConstants.REPORT_BY_STATUS);
        } else if (byAdmin) {
        	 filterRequest.setFieldToSumarize(adminUnit);
        }
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
        this.resultsByUser.clear();
        this.setDefaultPage();
        this.sumarizeResolutionsByUser(idResolution);
        this.currentResolution = resolution;
    }

    @Command
    @NotifyChange({ "currentStatus", "statusByUser", "filterActive", "statusByPending" })
    public void searchStatusByUser(@BindingParam("status") String status) {
        this.statusByUser.clear();
        this.statusByPending.clear();
        this.setDefaultPage();
        this.sumarizeStatusByUser(status);
        this.sumarizeStatusByPending(status);
        this.currentStatus = status;
    }

    private PageResponse<ResultsListView> sumarizeStatusByPending(String status) {
        CasesFilterRequest filterRequest = new CasesFilterRequest();
        PageRequest pageRequest = new PageRequest(activePage, pageSize, "");
        filterRequest.setPageRequest(pageRequest);
        filterRequest.setFilterActive(filterActive);
        setFilters(filterRequest);
        filterRequest.setStatus(status);
        filterRequest.setPendingResponse("true");
        filterRequest.setFieldToSumarize(UserConstants.REPORT_BY_PENDING);

        PageResponse<ResultsListView> pageResponse = resultsService.sumarizeAll(filterRequest);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.statusByPending.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    @Command
    @NotifyChange({ "byResolution", "resultsByUser", "currentResolution", "resultsByUser", "resultsState",
            "statusByUser", "currentStatus", "byMap", "byAdmin", "byAdmin", "byState", "showFilters",
            "statusByPending", "adminUnits" })
    public void showOption() {
        this.showFilters = true;
        this.filterActive = false;
        switch (this.reportType) {
            case REPORT_OPTION_RESOLUTION:
                this.byResolution = true;
                this.byAdmin = false;
                this.byState = false;
                this.results.clear();
                this.resultsByUser.clear();
                this.currentResolution = "";
                this.byMap = false;
                break;

            case REPORT_OPTION_STATE:
                this.byResolution = false;
                this.byAdmin = false;
                this.byState = true;
                this.byMap = false;
                this.resultsState.clear();
                this.statusByUser.clear();
                this.statusByPending.clear();
                this.currentStatus = "";
                break;
            case REPORT_OPTION_MAP:
                this.byResolution = false;
                this.byAdmin = false;
                this.byState = false;
                this.byMap = true;
                // this.filterActive = true;
                break;
            case REPORT_OPTION_ADMIN:
                this.byResolution = false;
                this.byAdmin = false;
                this.byState = false;
                this.byMap = false;
                this.adminUnits.clear();
                this.byAdmin = true;

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

    @Command
    @NotifyChange({ "triggers", "segments", "trigger", "segment" })
    public void selectDomain(@BindingParam("domain") DomainListView domain) {
        triggers.clear();
        segments.clear();
        if (domain != null && !domain.getId().equals(NOT_ID_ITEM_VALUE)) {
            triggers = getTriggerList(domain.getId());
        }
        setTrigger(null);
        setSegment(null);
    }

    public List<TriggerListView> getTriggers() {
        return triggers;
    }

    private List<TriggerListView> getTriggerList(String domainId) {
        List<TriggerListView> triggers = new ArrayList<>();
        PageResponse<TriggerListView> triggersResponse = triggerService.findAll(domainId, 0, -1, null);
        triggers = triggersResponse.getItems();
        if (triggers != null && !triggers.isEmpty()) {
            TriggerListView triggerBase = new TriggerListView();
            triggerBase.setId(NOT_ID_ITEM_VALUE);
            triggerBase.setName(Labels.getLabel(CASES_ALL_LABEL_KEY));
            triggers.add(0, triggerBase);
        }
        return triggers;
    }

    @Command
    @NotifyChange({ "segments", "trigger", "segment" })
    public void selectTrigger(@BindingParam("trigger") TriggerListView trigger) {
        segments.clear();
        if (trigger != null && !trigger.getId().equals(NOT_ID_ITEM_VALUE)) {
            segments = getSegments(domain.getId(), trigger.getId());
        }
        setSegment(null);
    }

    private List<SegmentListView> getSegments(String domainId, String triggerId) {
        List<SegmentListView> segments = new ArrayList<>();
        segments = triggerService.findSegments(domainId, triggerId);
        if (segments != null && !segments.isEmpty()) {
            SegmentListView segmentBase = new SegmentListView();
            segmentBase.setId(NOT_ID_ITEM_VALUE);
            segmentBase.setDescription(Labels.getLabel(CASES_ALL_LABEL_KEY));
            segments.add(0, segmentBase);
        }
        return segments;
    }

    private List<UserListView> getUsersList() {
        List<UserListView> users = new ArrayList<>();
        users = userService.findAll(null);

        if (users != null && !users.isEmpty()) {
            UserListView userBase = new UserListView();
            userBase.setId(NOT_ID_INT_VALUE);
            userBase.setUsername(Labels.getLabel(CASES_ALL_LABEL_KEY));
            users.add(0, userBase);
        }
        return users;
    }

    public UserListView getUserSelected() {
        return userSelected;
    }

    public void setUserSelected(UserListView userSelected) {
        this.userSelected = userSelected;
    }

    private List<String> getPendingOptionsList() {
        String[] options = { ALL_OPTION_VALUE, TRUE_OPTION_VALUE, FALSE_OPTION_VALUE };
        return new ArrayList<String>(Arrays.asList(options));
    }

    private List<String> getStatusOptionsList() {
        String[] options = { ALL_OPTION_VALUE, TRUE_OPTION_VALUE, FALSE_OPTION_VALUE };
        return new ArrayList<String>(Arrays.asList(options));
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    private List<String> getPriorityOptionsList() {
        String[] options = { ALL_OPTION_VALUE, HIGH_OPTION_VALUE, MEDIUM_OPTION_VALUE, LOW_OPTION_VALUE };
        return new ArrayList<String>(Arrays.asList(options));
    }

    private List<String> getAdminUnitsOptionsList() {
        String[] options = { UserConstants.REPORT_BY_UA_CONTINENT, UserConstants.REPORT_BY_UA_COUNTRY,
                UserConstants.REPORT_BY_UA_CITY, UserConstants.REPORT_BY_UA_NEIGHBORHOOD,
                UserConstants.REPORT_BY_UA_ADM1, UserConstants.REPORT_BY_UA_ADM2, UserConstants.REPORT_BY_UA_ADM3,
                UserConstants.REPORT_BY_UA_ADM4 };
        return new ArrayList<String>(Arrays.asList(options));
    }

    private void setFilters(CasesFilterRequest filterRequest) {
        if (Strings.isNotEmpty(this.title)) {
            filterRequest.setTitle(this.title);
        }

        if (this.domain != null && !this.domain.getId().equals(NOT_ID_ITEM_VALUE)) {
            filterRequest.setDomain(this.domain.getId());
        }

        if (this.trigger != null && !this.trigger.getId().equals(NOT_ID_ITEM_VALUE)) {
            filterRequest.setTrigger(this.trigger.getId());
        }

        if (this.segment != null && !this.segment.getId().equals(NOT_ID_ITEM_VALUE)) {
            filterRequest.setSegment(this.segment.getId());
        }

        if (!this.byResolution) {
            if (pendingResponse != null && !pendingResponse.equals(ALL_OPTION_VALUE)) {
                filterRequest.setPendingResponse(this.pendingResponse);
            }

            if (status != null && !status.equals(ALL_OPTION_VALUE)) {
                filterRequest.setStatus(this.status);
            }
        }

        if (userSelected != null && !userSelected.getId().equals(NOT_ID_INT_VALUE)) {
            filterRequest.setUserSelected(this.userSelected.getUsername());
        }

        if (priority != null && !priority.equals(ALL_OPTION_VALUE)) {
            filterRequest.setPriority(this.priority);
        }

        if (this.fromDate != null) {
            filterRequest.setFromDate(this.fromDate);
        }

        if (this.toDate != null) {
            filterRequest.setToDate(this.toDate);
        }

        if (Strings.isNotEmpty(this.subject)) {
            filterRequest.setSubject(this.subject);
        }
    }

    public List<ResultsListView> getResults() {
        return results;
    }

    public void setResults(List<ResultsListView> results) {
        this.results = results;
    }

    public List<ResultsListView> getResultsState() {
        return resultsState;
    }

    private void setDefaultPage() {
        this.pageSize = PAGE_SIZE_DEFAULT;
        this.activePage = ACTIVE_PAGE_DEFAULT;
    }

    private List<String> getResultsOptionsList() {
        String[] options = { REPORT_OPTION_RESOLUTION, REPORT_OPTION_ADMIN, REPORT_OPTION_STATE, REPORT_OPTION_MAP };
        return new ArrayList<String>(Arrays.asList(options));
    }

    public boolean isFilterActive() {
        return filterActive;
    }

    public void setFilterActive(boolean filterActive) {
        this.filterActive = filterActive;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPendingResponse() {
        return pendingResponse;
    }

    public void setPendingResponse(String pendingResponse) {
        this.pendingResponse = pendingResponse;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public TriggerListView getTrigger() {
        return trigger;
    }

    public void setTrigger(TriggerListView trigger) {
        this.trigger = trigger;
    }

    public List<SegmentListView> getSegments() {
        return segments;
    }

    public void setSegments(List<SegmentListView> segments) {
        this.segments = segments;
    }

    public SegmentListView getSegment() {
        return segment;
    }

    public void setSegment(SegmentListView segment) {
        this.segment = segment;
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

    public DateConverter getDateConverter() {
        return dateConverter;
    }

    public List<UserListView> getUsers() {
        return users;
    }

    public List<String> getPendingOptions() {
        return pendingOptions;
    }

    public List<String> getStatusOptions() {
        return statusOptions;
    }

    public List<String> getPriorityOptions() {
        return priorityOptions;
    }

    public List<ResultsListView> getStatusByUser() {
        return statusByUser;
    }

    public boolean isShowFilters() {
        return showFilters;
    }

    public void setShowFilters(boolean showFilters) {
        this.showFilters = showFilters;
    }

    public List<ResultsListView> getStatusByPending() {
        return statusByPending;
    }

    public boolean isByMap() {
        return byMap;
    }

    public void setByMap(boolean byMap) {
        this.byMap = byMap;
    }

    @Command
    @NotifyChange("geoJson")
    public void searchByMap() {
        PageRequest pageRequest = new PageRequest(0, 1000, "openDate");
        CasesFilterRequest filterRequest = new CasesFilterRequest();
        filterRequest.setPageRequest(pageRequest);
        filterRequest.setFilterActive(filterActive);
        setFilters(filterRequest);
        geoJson = caseService.calculateGeoJson(filterRequest);
        if (geoJson.isEmpty()) {
            geoJson = null;
        }
    }

    public String getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(String geoJson) {
        this.geoJson = geoJson;
    }

    public List<ResultsListView> getAdminUnits() {
        return adminUnits;
    }

    public List<String> getAdminUnitsOptions() {
        return adminUnitsOptions;
    }

    public String getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(String adminUnit) {
        this.adminUnit = adminUnit;
    }
}
