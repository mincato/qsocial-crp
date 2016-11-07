package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;

import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.SegmentListView;
import com.qsocialnow.common.model.config.Series;
import com.qsocialnow.common.model.config.SubSeries;
import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.converters.DateConverter;
import com.qsocialnow.model.BaseOptionElement;
import com.qsocialnow.model.Connotation;
import com.qsocialnow.model.FilterView;
import com.qsocialnow.model.Language;
import com.qsocialnow.security.LoginConfig;
import com.qsocialnow.services.CaseCategorySetService;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.SubjectCategorySetService;
import com.qsocialnow.services.SubjectService;
import com.qsocialnow.services.ThematicService;
import com.qsocialnow.services.TriggerService;
import com.qsocialnow.services.UserService;
import com.qsocialnow.services.UserSessionService;

@VariableResolver(DelegatingVariableResolver.class)
public class CasesViewModel implements Serializable {

    private static final String CASES_LIST_FILTER_LABEL = "cases.list.filter.";

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

    // Advance filter
    private List<String> advancedFilter = new ArrayList<>();

    private List<CaseCategory> caseCategoriesOptions = new ArrayList<>();

    private CaseCategory caseCategory;

    private List<SubjectCategory> subjectCategoriesOptions = new ArrayList<>();

    private SubjectCategory subjectCategory;

    private FilterView filterView;

    private List<BaseOptionElement> sourceOptions;

    private BaseOptionElement source;

    private List<BaseOptionElement> languageOptions;

    private BaseOptionElement language;

    private List<BaseOptionElement> connotationOptions;

    private BaseOptionElement connotation;

    private ListModelList<Thematic> thematicsOptions = new ListModelList<>();

    private ListModelList<Series> serieOptions = new ListModelList<>();

    private ListModelList<SubSeries> subSerieOptions = new ListModelList<>();

    private String text;

    private String mentions;

    private String hashTags;

    private String authors;

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

    @WireVariable
    private CaseCategorySetService caseCategorySetService;

    @WireVariable
    private SubjectCategorySetService subjectCategorySetService;

    @WireVariable
    private ThematicService thematicService;

    @Init
    public void init() {
        this.initUserInformation();
        this.domains = getDomainsList();
        this.users = getUsersList();
        this.filterActive = false;
        this.filterView = new FilterView();
        this.pendingOptions = getPendingOptionsList();
        this.statusOptions = getStatusOptionsList();
        this.priorityOptions = getPriorityOptionsList();
        this.caseCategoriesOptions = getCaseCategoriesOptionsList();
        this.subjectCategoriesOptions = getSubjectCategoriesOptionsList();
        this.sourceOptions = getSourceOptionsList();
        this.languageOptions = getLanguageOptionsList();
        this.connotationOptions = getConnotationOptionsList();
        this.thematicsOptions = getThematicsOptionsList();

        findCases();
        this.dateConverter = new DateConverter(userSessionService.getTimeZone());
    }

    private ListModelList<Thematic> getThematicsOptionsList() {
        ListModelList<Thematic> thematicsOptions = new ListModelList<>();
        thematicsOptions.add(new Thematic());
        List<Thematic> allThematics = thematicService.findAll();
        thematicsOptions.addAll(allThematics);
        return thematicsOptions;
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
        byte[] data = caseService.getReport(getFilters(), userSessionService.getLanguage());
        Filedownload.save(data, "application/vnd.ms-excel", "file.xls");
    }

    @Command
    @NotifyChange({ "serieOptions", "subSerieOptions" })
    public void selectThematic(@BindingParam("fxFilter") FilterView fxFilter) {
        serieOptions.clear();
        if (fxFilter.getThematic() != null && fxFilter.getThematic().getId() != null && serieOptions.isEmpty()) {
            serieOptions.add(new Series());
            serieOptions.addAll(fxFilter.getThematic().getSeries());
        }
        fxFilter.setSerie(null);
        selectSerie(fxFilter);
        BindUtils.postNotifyChange(null, null, fxFilter, "serie");
    }

    @Command
    @NotifyChange({ "subSerieOptions" })
    public void selectSerie(@BindingParam("fxFilter") FilterView fxFilter) {
        subSerieOptions.clear();
        if (fxFilter.getSerie() != null && fxFilter.getSerie().getId() != null && subSerieOptions.isEmpty()) {
            subSerieOptions.add(new SubSeries());
            subSerieOptions.addAll(fxFilter.getSerie().getSubSeries());
        }
        fxFilter.setSubSerie(null);
        BindUtils.postNotifyChange(null, null, fxFilter, "subSerie");
        BindUtils.postNotifyChange(null, null, fxFilter, "filterCategories");
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

        if (this.caseCategory != null && !this.caseCategory.getId().equals(NOT_ID_ITEM_VALUE)) {
            filters.put("caseCategory", this.caseCategory.getId());
        }

        if (this.subjectCategory != null && !this.subjectCategory.getId().equals(NOT_ID_ITEM_VALUE)) {
            filters.put("subjectCategory", this.subjectCategory.getId());
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

    private List<CaseCategory> getCaseCategoriesOptionsList() {
        List<CaseCategory> categories = new ArrayList<>();
        categories = caseCategorySetService.findAllCategories();

        if (categories != null && !categories.isEmpty()) {
            CaseCategory categoryBase = new CaseCategory();
            categoryBase.setId(NOT_ID_ITEM_VALUE);
            categoryBase.setDescription(Labels.getLabel(CASES_ALL_LABEL_KEY));
            categories.add(0, categoryBase);
        }
        return categories;
    }

    public DateConverter getDateConverter() {
        return dateConverter;
    }

    private List<SubjectCategory> getSubjectCategoriesOptionsList() {
        List<SubjectCategory> categories = new ArrayList<>();
        categories = subjectCategorySetService.findAllCategories();

        if (categories != null && !categories.isEmpty()) {
            SubjectCategory categoryBase = new SubjectCategory();
            categoryBase.setId(NOT_ID_ITEM_VALUE);
            categoryBase.setDescription(Labels.getLabel(CASES_ALL_LABEL_KEY));
            categories.add(0, categoryBase);
        }
        return categories;
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

    private List<BaseOptionElement> getSourceOptionsList() {
        List<BaseOptionElement> options = new ArrayList<>();
        for (Media media : Media.values()) {
            BaseOptionElement option = new BaseOptionElement();
            option.setId(String.valueOf(media.getValue()));
            option.setText(media.getName());
            options.add(option);
        }
        if (!options.isEmpty()) {
            BaseOptionElement option = new BaseOptionElement();
            option.setId(NOT_ID_ITEM_VALUE);
            option.setText(Labels.getLabel(CASES_ALL_LABEL_KEY));
            options.add(0, option);
        }
        return options;
    }

    private List<BaseOptionElement> getLanguageOptionsList() {
        List<BaseOptionElement> options = new ArrayList<>();
        for (Language language : Language.values()) {
            BaseOptionElement option = new BaseOptionElement();
            option.setId(String.valueOf(language.getValue()));
            option.setText(Labels.getLabel(CASES_LIST_FILTER_LABEL + language.getImage()));
            options.add(option);
        }
        if (!options.isEmpty()) {
            BaseOptionElement option = new BaseOptionElement();
            option.setId(NOT_ID_ITEM_VALUE);
            option.setText(Labels.getLabel(CASES_ALL_LABEL_KEY));
            options.add(0, option);
        }
        return options;
    }

    private List<BaseOptionElement> getConnotationOptionsList() {
        List<BaseOptionElement> options = new ArrayList<>();
        for (Connotation connotation : Connotation.values()) {
            BaseOptionElement option = new BaseOptionElement();
            option.setId(String.valueOf(connotation.getValue()));
            option.setText(Labels.getLabel(CASES_LIST_FILTER_LABEL + connotation.getName()));
            options.add(option);
        }
        if (!options.isEmpty()) {
            BaseOptionElement option = new BaseOptionElement();
            option.setId(NOT_ID_ITEM_VALUE);
            option.setText(Labels.getLabel(CASES_ALL_LABEL_KEY));
            options.add(0, option);
        }
        return options;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public List<String> getAdvancedFilter() {
        return advancedFilter;
    }

    public void setAdvancedFilter(List<String> advancedFilter) {
        this.advancedFilter = advancedFilter;
    }

    public List<CaseCategory> getCaseCategoriesOptions() {
        return caseCategoriesOptions;
    }

    public CaseCategory getCaseCategory() {
        return caseCategory;
    }

    public void setCaseCategory(CaseCategory caseCategory) {
        this.caseCategory = caseCategory;
    }

    public List<SubjectCategory> getSubjectCategoriesOptions() {
        return subjectCategoriesOptions;
    }

    public SubjectCategory getSubjectCategory() {
        return subjectCategory;
    }

    public void setSubjectCategory(SubjectCategory subjectCategory) {
        this.subjectCategory = subjectCategory;
    }

    public FilterView getFilter() {
        return filterView;
    }

    public void setFilter(FilterView filterView) {
        this.filterView = filterView;
    }

    public List<BaseOptionElement> getSourceOptions() {
        return sourceOptions;
    }

    public BaseOptionElement getSource() {
        return source;
    }

    public void setSource(BaseOptionElement source) {
        this.source = source;
    }

    public BaseOptionElement getLanguage() {
        return language;
    }

    public void setLanguage(BaseOptionElement language) {
        this.language = language;
    }

    public List<BaseOptionElement> getLanguageOptions() {
        return languageOptions;
    }

    public BaseOptionElement getConnotation() {
        return connotation;
    }

    public void setConnotation(BaseOptionElement connotation) {
        this.connotation = connotation;
    }

    public List<BaseOptionElement> getConnotationOptions() {
        return connotationOptions;
    }

    public ListModelList<Thematic> getThematicsOptions() {
        return thematicsOptions;
    }

    public ListModelList<Series> getSerieOptions() {
        return serieOptions;
    }

    public ListModelList<SubSeries> getSubSerieOptions() {
        return subSerieOptions;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMentions() {
        return mentions;
    }

    public void setMentions(String mentions) {
        this.mentions = mentions;
    }

    public String getHashTags() {
        return hashTags;
    }

    public void setHashTags(String hashTags) {
        this.hashTags = hashTags;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }
}
