package com.qsocialnow.viewmodel;

import java.awt.font.TextHitInfo;
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
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Filedownload;

import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.SegmentListView;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.converters.DateConverter;
import com.qsocialnow.security.LoginConfig;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.SubjectService;
import com.qsocialnow.services.TriggerService;
import com.qsocialnow.services.UserService;
import com.qsocialnow.services.UserSessionService;

@VariableResolver(DelegatingVariableResolver.class)
public class CasesViewModel implements Serializable {

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

    private static final long serialVersionUID = 2259179419421396093L;

    private static final int PAGE_SIZE_DEFAULT = 15;

    private static final int ACTIVE_PAGE_DEFAULT = 0;

    private int pageSize = PAGE_SIZE_DEFAULT;

    private int activePage = ACTIVE_PAGE_DEFAULT;

    private String sortField = "openDate";

    private boolean sortOrder = false;

    @WireVariable
    private CaseService caseService;

    @WireVariable
    private SubjectService subjectService;

    private boolean moreResults;

    private List<CaseListView> cases = new ArrayList<>();

    // filters
    private boolean filterActive;

    private UserListView userSelected;

    private List<UserListView> users = new ArrayList<>();

    private Long fromDate;

    private Long toDate;

    private String title;

    private String description;

    private String subject;

    private String userName;

    private boolean isAdmin;

    private List<String> statusOptions = new ArrayList<>();

    private String status;

    private List<String> priorityOptions = new ArrayList<>();

    private String priority;

    private List<String> pendingOptions = new ArrayList<>();

    private String pendingResponse;

    private List<DomainListView> domains = new ArrayList<>();

    private DomainListView domain;

    private List<TriggerListView> triggers = new ArrayList<>();

    private TriggerListView trigger;

    private List<SegmentListView> segments = new ArrayList<>();

    private SegmentListView segment;

    @WireVariable
    private UserSessionService userSessionService;

    @WireVariable
    private UserService userService;

    @WireVariable
    private LoginConfig loginConfig;

    @WireVariable
    private DomainService domainService;

    @WireVariable
    private TriggerService triggerService;

    @Init
    public void init() {
        this.initUserInformation();
        this.domains = getDomainsList();
        this.users = getUsersList();
        this.filterActive = false;
        this.pendingOptions = getPendingOptionsList();
        this.statusOptions = getStatusOptionsList();
        this.priorityOptions = getPriorityOptionsList();
        findCases();
        this.dateConverter = new DateConverter(userSessionService.getTimeZone());
    }

    private void initUserInformation() {
        this.userName = userSessionService.getUsername();
        this.isAdmin = userSessionService.isAdmin();
    }

    @Command
    @NotifyChange({ "cases", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findCases();
    }

    @Command
    @NotifyChange({ "cases", "moreResults", "sortField", "sortOrder" })
    public void sortList(@BindingParam("sortField") String sortField) {
        this.sortField = sortField;
        this.sortOrder = !this.sortOrder;
        this.activePage = 0;
        this.cases.clear();
        this.findCases();
    }

    private PageResponse<CaseListView> findCases() {
        PageRequest pageRequest = new PageRequest(activePage, pageSize, sortField);
        pageRequest.setSortOrder(this.sortOrder);
        PageResponse<CaseListView> pageResponse = caseService.findAll(pageRequest, filterActive ? getFilters() : null);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.cases.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    @Command
    @NotifyChange({ "cases", "moreResults", "filterActive" })
    public void search() {
        this.filterActive = true;
        this.setDefaultPage();
        this.cases.clear();
        this.findCases();
    }

    @Command
    public void download() {
        byte[] data = caseService.getReport(getFilters());
        Filedownload.save(data, "application/vnd.ms-excel", "file.xls");
    }

    private Map<String, String> getFilters() {
        Map<String, String> filters = new HashMap<String, String>();
        if (this.title != null && !this.title.isEmpty()) {
            filters.put("title", this.title);
        }

        if (this.domain != null && !this.domain.getId().equals(NOT_ID_ITEM_VALUE)) {
            filters.put("domainId", this.domain.getId());
        }

        if (this.trigger != null && !this.trigger.getId().equals(NOT_ID_ITEM_VALUE)) {
            filters.put("triggerId", this.trigger.getId());
        }

        if (this.segment != null && !this.segment.getId().equals(NOT_ID_ITEM_VALUE)) {
            filters.put("segmentId", this.segment.getId());
        }

        if (pendingResponse != null && !pendingResponse.equals(ALL_OPTION_VALUE)) {
            filters.put("pendingResponse", this.pendingResponse);
        }

        if (userSelected != null && !userSelected.getId().equals(NOT_ID_INT_VALUE)) {
            filters.put("userSelected", this.userSelected.getUsername());
        }

        if (status != null && !status.equals(ALL_OPTION_VALUE)) {
            filters.put("status", this.status);
        }

        if (priority != null && !priority.equals(ALL_OPTION_VALUE)) {
            filters.put("priority", this.priority);
        }

        if (this.fromDate != null) {
            filters.put("fromOpenDate", String.valueOf(this.fromDate));
        }

        if (this.toDate != null) {
            filters.put("toOpenDate", String.valueOf(this.toDate));
        }

        if (this.subject != null) {
            filters.put("subject", this.subject);
        }

        return filters;
    }

    private void setDefaultPage() {
        this.pageSize = PAGE_SIZE_DEFAULT;
        this.activePage = ACTIVE_PAGE_DEFAULT;
    }

    private List<String> getStatusOptionsList() {
        String[] options = { ALL_OPTION_VALUE, TRUE_OPTION_VALUE, FALSE_OPTION_VALUE };
        return new ArrayList<String>(Arrays.asList(options));
    }

    private List<String> getPriorityOptionsList() {
        String[] options = { ALL_OPTION_VALUE, HIGH_OPTION_VALUE, MEDIUM_OPTION_VALUE, LOW_OPTION_VALUE };
        return new ArrayList<String>(Arrays.asList(options));
    }

    public DateConverter getDateConverter() {
        return dateConverter;
    }

    private List<DomainListView> getDomainsList() {
        List<DomainListView> domains = new ArrayList<>();
        PageResponse<DomainListView> domainsResponse;
        if (isAdmin)
            domainsResponse = domainService.findAll();
        else
            domainsResponse = domainService.findAllByUserNameAllowed(this.userName);

        domains = domainsResponse.getItems();
        if (domains != null) {
            DomainListView domainBase = new DomainListView();
            domainBase.setId(NOT_ID_ITEM_VALUE);
            domainBase.setName(Labels.getLabel(CASES_ALL_LABEL_KEY));
            domains.add(0, domainBase);
        }
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
            segmentBase.setDescription("all");
            segmentBase.setDescription(Labels.getLabel(CASES_ALL_LABEL_KEY));
            segments.add(0, segmentBase);
        }
        return segments;
    }

    private List<UserListView> getUsersList() {
        List<UserListView> users = new ArrayList<>();
        if (isAdmin)
            users = userService.findAll(null);
        else
            users = userService.findAllByUserName(userName);

        if (users != null && !users.isEmpty()) {
            UserListView userBase = new UserListView();
            userBase.setId(NOT_ID_INT_VALUE);
            userBase.setUsername(Labels.getLabel(CASES_ALL_LABEL_KEY));
            users.add(0, userBase);
        }
        return users;
    }

    public List<CaseListView> getCases() {
        return this.cases;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public boolean isMoreResults() {
        return moreResults;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List<String> getStatusOptions() {
        return statusOptions;
    }

    public void setStatusOptions(List<String> statusOptions) {
        this.statusOptions = statusOptions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPendingResponse() {
        return pendingResponse;
    }

    public void setPendingResponse(String pendingResponse) {
        this.pendingResponse = pendingResponse;
    }

    public List<String> getPendingOptions() {
        return pendingOptions;
    }

    public void setPendingOptions(List<String> pendingOptions) {
        this.pendingOptions = pendingOptions;
    }

    private List<String> getPendingOptionsList() {
        String[] options = { ALL_OPTION_VALUE, TRUE_OPTION_VALUE, FALSE_OPTION_VALUE };
        return new ArrayList<String>(Arrays.asList(options));
    }

    public DomainListView getDomain() {
        return domain;
    }

    public void setDomain(DomainListView domain) {
        this.domain = domain;
    }

    public List<TriggerListView> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<TriggerListView> triggers) {
        this.triggers = triggers;
    }

    public List<SegmentListView> getSegments() {
        return segments;
    }

    public void setSegments(List<SegmentListView> segments) {
        this.segments = segments;
    }

    public List<DomainListView> getDomains() {
        return domains;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public boolean isSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(boolean sortOrder) {
        this.sortOrder = sortOrder;
    }

    public UserListView getUserSelected() {
        return userSelected;
    }

    public void setUserSelected(UserListView userSelected) {
        this.userSelected = userSelected;
    }

    public List<UserListView> getUsers() {
        return users;
    }

    public void setUsers(List<UserListView> users) {
        this.users = users;
    }

    public void setDomains(List<DomainListView> domains) {
        this.domains = domains;
    }

    public TriggerListView getTrigger() {
        return trigger;
    }

    public void setTrigger(TriggerListView trigger) {
        this.trigger = trigger;
    }

    public SegmentListView getSegment() {
        return segment;
    }

    public void setSegment(SegmentListView segment) {
        this.segment = segment;
    }

    public List<String> getPriorityOptions() {
        return priorityOptions;
    }

    public void setPriorityOptions(List<String> priorityOptions) {
        this.priorityOptions = priorityOptions;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
