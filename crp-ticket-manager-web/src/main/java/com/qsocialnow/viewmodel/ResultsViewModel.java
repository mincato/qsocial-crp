package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
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

import com.qsocialnow.common.model.cases.CaseAggregationReport;
import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.cases.CasesFilterRequestReport;
import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.config.AdmUnitFilter;
import com.qsocialnow.common.model.config.AdminUnit;
import com.qsocialnow.common.model.config.BaseAdminUnit;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.SegmentListView;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.common.model.filter.AdministrativeUnitsFilter;
import com.qsocialnow.common.model.filter.AdministrativeUnitsFilterBean;
import com.qsocialnow.common.model.filter.FilterNormalizer;
import com.qsocialnow.common.model.filter.FollowersCountRange;
import com.qsocialnow.common.model.filter.RangeRequest;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.util.UserConstants;
import com.qsocialnow.converters.DateConverter;
import com.qsocialnow.model.MediaView;
import com.qsocialnow.services.AdminUnitsService;
import com.qsocialnow.services.AutocompleteService;
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

    private List<String> adminUnitsTypes = new ArrayList<>();

    private List<CasesReportOption> resultsTypeOptions = new ArrayList<>();

    private Long fromDate;

    private Long toDate;

    private String title;

    private String subject;

    private String userName;

    private String status;

    private String priority;

    private String pendingResponse;

    private String adminUnitType;

    private Long followersGreaterThan;

    private Long followersLessThan;

    private List<MediaView> mediaTypes;

    private CasesReportOption reportType;

    private AutocompleteListModel<AdminUnit> adminUnitsOptions;

    private AdminUnit adminUnitOption;

    @WireVariable
    private AutocompleteService<AdminUnit> adminUnitsAutocompleteService;

    @WireVariable
    private UserSessionService userSessionService;

    @WireVariable
    private TriggerService triggerService;

    @WireVariable
    private UserService userService;

    @WireVariable
    private CaseService caseService;

    @WireVariable
    private AdminUnitsService adminUnitsService;

    @WireVariable
    private FilterNormalizer filterNormalizer;

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
        this.adminUnitsTypes = getAdminUnitsTypeList();
        this.dateConverter = new DateConverter(userSessionService.getTimeZone());
        this.adminUnitsOptions = new AutocompleteListModel<AdminUnit>(adminUnitsAutocompleteService,
                userSessionService.getLanguage());
        initMediaTypesOptions();
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
        filterRequest.setFieldToSumarize(adminUnitType);
        PageResponse<ResultsListView> pageResponse = resultsService.sumarizeAll(filterRequest);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            List<ResultsListView> items = pageResponse.getItems();
            List<ResultsListView> results = setUnitAdminName(items);
            this.adminUnits.addAll(results);
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    private List<ResultsListView> setUnitAdminName(List<ResultsListView> items) {
        ObjectMapper mapper = new ObjectMapper();
        List<ResultsListView> results = mapper.convertValue(items, new TypeReference<List<ResultsListView>>() {
        });
        List<String> ids = results.stream().map(ResultsListView::getUnitAdmin).collect(Collectors.toList());

        String query = StringUtils.join(ids, ",");
        List<AdminUnit> adminRanking = adminUnitsService
                .findUnitAdminsByGeoIds(query, userSessionService.getLanguage());

        Map<Long, String> adminUnitById = adminRanking.stream().collect(
                Collectors.toMap(AdminUnit::getGeoNameId, AdminUnit::getTranslation));

        results.stream().forEach(result -> result.setUnitAdmin(adminUnitById.get(Long.valueOf(result.getUnitAdmin()))));
        return results;
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
        if (reportType.isByResolution()) {
            this.results.clear();
            this.resultsByUser.clear();
            this.currentResolution = "";
            this.sumarizeCases();
        } else if (reportType.isByState()) {
            this.resultsState.clear();
            this.statusByUser.clear();
            this.currentStatus = "";
            this.sumarizeCasesByStatus();
        } else if (reportType.isByAdmin()) {
            this.adminUnits.clear();
            this.sumarizeCasesByUnitAdmin();
        }
    }

    @Command
    public void download() {
        CasesFilterRequest filterRequest = new CasesFilterRequest();
        filterRequest.setFilterActive(filterActive);
        setFilters(filterRequest);
        CasesFilterRequestReport filterRequestReport = new CasesFilterRequestReport();
        filterRequestReport.setFilterRequest(filterRequest);
        filterRequestReport.setLanguage(userSessionService.getLanguage());
        byte[] data = null;
        if (reportType.isByResolution()) {
            filterRequest.setFieldToSumarize(UserConstants.REPORT_BY_RESOLUTION);
            data = resultsService.getReport(filterRequestReport);
        } else if (reportType.isByState()) {
            filterRequest.setFieldToSumarize(UserConstants.REPORT_BY_STATUS);
            data = resultsService.getReport(filterRequestReport);
        } else if (reportType.isByAdmin()) {
            data = getReportByAdministrativeUnit(filterRequest, data);
        }
        if (data != null) {
            Filedownload.save(data, "application/vnd.ms-excel", "file.xls");
        }
    }

    private byte[] getReportByAdministrativeUnit(CasesFilterRequest filterRequest, byte[] data) {
        PageRequest pageRequest = new PageRequest(0, 10, "");
        filterRequest.setPageRequest(pageRequest);
        filterRequest.setFieldToSumarize(adminUnitType);
        PageResponse<ResultsListView> pageResponse = resultsService.sumarizeAll(filterRequest);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            List<ResultsListView> results = setUnitAdminName(pageResponse.getItems());
            CaseAggregationReport caseAggregationReport = new CaseAggregationReport();
            caseAggregationReport.setItems(results);
            caseAggregationReport.setLanguage(userSessionService.getLanguage());
            data = resultsService.getReportByAdministrativeUnit(caseAggregationReport);
        }
        return data;
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
    @NotifyChange({ "resultsByUser", "currentResolution", "resultsByUser", "resultsState", "statusByUser",
            "currentStatus", "showFilters", "statusByPending", "adminUnits", "geoJson", "filterActive", "results" })
    public void showOption() {
        this.showFilters = true;
        this.filterActive = false;
        this.geoJson = null;
        if (!CollectionUtils.isEmpty(this.results)) {
            this.results.clear();
        }
        if (!CollectionUtils.isEmpty(this.resultsByUser)) {
            this.resultsByUser.clear();
        }
        this.currentResolution = "";
        if (!CollectionUtils.isEmpty(this.resultsState)) {
            this.resultsState.clear();
        }
        if (!CollectionUtils.isEmpty(this.statusByUser)) {
            this.statusByUser.clear();
        }
        if (!CollectionUtils.isEmpty(this.statusByPending)) {
            this.statusByPending.clear();
        }
        if (!CollectionUtils.isEmpty(this.adminUnits)) {
            this.adminUnits.clear();
        }
        this.currentStatus = "";

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

    private List<String> getAdminUnitsTypeList() {
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

        if (!this.reportType.isByResolution()) {
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

        if (this.reportType.isByMap()) {
            addAdmUnitFilters(filterRequest, adminUnitOption);
            addFollowersFilter(filterRequest, followersGreaterThan, followersLessThan);
            addMediaFilter(filterRequest, mediaTypes);
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

    private List<CasesReportOption> getResultsOptionsList() {
        return new ArrayList<CasesReportOption>(Arrays.asList(CasesReportOption.values()));
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

    public List<CasesReportOption> getResultsTypeOptions() {
        return resultsTypeOptions;
    }

    public void setResultsTypeOptions(List<CasesReportOption> resultsTypeOptions) {
        this.resultsTypeOptions = resultsTypeOptions;
    }

    public CasesReportOption getReportType() {
        return reportType;
    }

    public void setReportType(CasesReportOption reportType) {
        this.reportType = reportType;
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

    @Command
    @NotifyChange({ "geoJson", "filterActive" })
    public void searchByMap() {
        this.filterActive = true;
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

    public List<String> getAdminUnitsTypes() {
        return adminUnitsTypes;
    }

    public String getAdminUnitType() {
        return adminUnitType;
    }

    public void setAdminUnitType(String adminUnitType) {
        this.adminUnitType = adminUnitType;
    }

    @Command
    public String createAdmUnitValue(AdminUnit adminUnit) {
        StringBuilder sb = new StringBuilder();
        addAdmUnitText(sb, adminUnit);
        return sb.toString();
    }

    @Command
    public String createAdmUnitDescription(AdminUnit adminUnit) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < adminUnit.getParents().size(); i++) {
            BaseAdminUnit baseAdminUnit = adminUnit.getParents().get(i);
            if (i > 0 && i < adminUnit.getParents().size() - 1) {
                sb.append(" - ");
            }
            addAdmUnitText(sb, baseAdminUnit);
        }
        return sb.toString();
    }

    private void addAdmUnitText(StringBuilder sb, BaseAdminUnit adminUnit) {
        sb.append(adminUnit.getTranslation());
        sb.append("(");
        sb.append(Labels.getLabel("trigger.criteria.admUnit.value." + adminUnit.getType().name()));
        sb.append(")");
    }

    private void addAdmUnitFilters(CasesFilterRequest request, AdminUnit adminUnit) {
        AdmUnitFilter admUnitFilter = new AdmUnitFilter();
        admUnitFilter.setAdminUnit(adminUnit);
        filterNormalizer.normalizeAdmUnitFilter(admUnitFilter);
        AdministrativeUnitsFilterBean administrativeUnitsFilterBean = new AdministrativeUnitsFilterBean();
        administrativeUnitsFilterBean.setAdm1(admUnitFilter.getAdm1());
        administrativeUnitsFilterBean.setAdm2(admUnitFilter.getAdm2());
        administrativeUnitsFilterBean.setAdm3(admUnitFilter.getAdm3());
        administrativeUnitsFilterBean.setAdm4(admUnitFilter.getAdm4());
        administrativeUnitsFilterBean.setContinent(admUnitFilter.getContinent());
        administrativeUnitsFilterBean.setCountry(admUnitFilter.getCountry());
        administrativeUnitsFilterBean.setCity(admUnitFilter.getCity());
        administrativeUnitsFilterBean.setNeighborhood(admUnitFilter.getNeighborhood());
        administrativeUnitsFilterBean.setAdminUnit(admUnitFilter.getAdminUnit());
        AdministrativeUnitsFilterBean[] administrativeUnitsFilterBeans = new AdministrativeUnitsFilterBean[1];
        administrativeUnitsFilterBeans[0] = administrativeUnitsFilterBean;
        AdministrativeUnitsFilter administrativeUnitsFilter = new AdministrativeUnitsFilter();
        administrativeUnitsFilter.setAdministrativeUnitsFilterBeans(administrativeUnitsFilterBeans);
        request.setAdministrativeUnitsFilter(administrativeUnitsFilter);
    }

    private void addFollowersFilter(CasesFilterRequest request, Long followersGreaterThan, Long followersLessThan) {
        if (followersGreaterThan != null || followersLessThan != null) {
            RangeRequest rangeRequest = new RangeRequest();
            FollowersCountRange followersCount = new FollowersCountRange();
            followersCount.setGt(followersGreaterThan);
            followersCount.setLt(followersLessThan);
            rangeRequest.setFollowersCount(followersCount);
            request.setRange(rangeRequest);
        }
    }

    private void addMediaFilter(CasesFilterRequest request, List<MediaView> mediaTypes) {
        List<MediaView> mediasPicked = mediaTypes.stream().filter(media -> media.isChecked())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(mediasPicked) && mediasPicked.size() < mediaTypes.size()) {
            Integer[] options = mediasPicked.stream().map(media -> media.getMedia().getValue().intValue())
                    .toArray(size -> new Integer[size]);
            request.setMediums(options);
        }
    }

    private void initMediaTypesOptions() {
        mediaTypes = new ArrayList<>();
        for (Media media : Media.values()) {
            MediaView mediaView = new MediaView();
            mediaView.setMedia(media);
            mediaView.setChecked(false);
            mediaTypes.add(mediaView);
        }
    }

    public AutocompleteListModel<AdminUnit> getAdminUnitsOptions() {
        return adminUnitsOptions;
    }

    public AdminUnit getAdminUnitOption() {
        return adminUnitOption;
    }

    public void setAdminUnitOption(AdminUnit adminUnitOption) {
        this.adminUnitOption = adminUnitOption;
    }

    public Long getFollowersGreaterThan() {
        return followersGreaterThan;
    }

    public void setFollowersGreaterThan(Long followersGreaterThan) {
        this.followersGreaterThan = followersGreaterThan;
    }

    public Long getFollowersLessThan() {
        return followersLessThan;
    }

    public void setFollowersLessThan(Long followersLessThan) {
        this.followersLessThan = followersLessThan;
    }

    public List<MediaView> getMediaTypes() {
        return mediaTypes;
    }

    public void setMediaTypes(List<MediaView> mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

}