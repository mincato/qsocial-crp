package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;

import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.config.AdminUnit;
import com.qsocialnow.common.model.config.BaseAdminUnit;
import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.Category;
import com.qsocialnow.common.model.config.CategoryGroup;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.NameByLanguage;
import com.qsocialnow.common.model.config.SegmentListView;
import com.qsocialnow.common.model.config.Series;
import com.qsocialnow.common.model.config.SubSeries;
import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.config.UserListView;
import com.qsocialnow.common.model.config.WordFilter;
import com.qsocialnow.common.model.config.WordFilterType;
import com.qsocialnow.common.model.filter.AdministrativeUnitsFilter;
import com.qsocialnow.common.model.filter.AdministrativeUnitsFilterBean;
import com.qsocialnow.common.model.filter.FilterNormalizer;
import com.qsocialnow.common.model.filter.FollowersCountRange;
import com.qsocialnow.common.model.filter.RangeRequest;
import com.qsocialnow.common.model.filter.WordsFilterRequestBean;
import com.qsocialnow.common.model.filter.WordsListFilterBean;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.converters.DateConverter;
import com.qsocialnow.model.AdmUnitFilterView;
import com.qsocialnow.model.CategoryFilterView;
import com.qsocialnow.model.Connotation;
import com.qsocialnow.model.ConnotationView;
import com.qsocialnow.model.FilterView;
import com.qsocialnow.model.Language;
import com.qsocialnow.model.LanguageView;
import com.qsocialnow.model.MediaView;
import com.qsocialnow.security.LoginConfig;
import com.qsocialnow.services.AutocompleteService;
import com.qsocialnow.services.CaseCategorySetService;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.DomainService;
import com.qsocialnow.services.SubjectCategorySetService;
import com.qsocialnow.services.SubjectService;
import com.qsocialnow.services.ThematicService;
import com.qsocialnow.services.TriggerService;
import com.qsocialnow.services.UserService;
import com.qsocialnow.services.UserSessionService;
import com.qsocialnow.util.DateTimeBoxComponent;

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

    // Advance filter
    private List<String> advancedFilter = new ArrayList<>();

    private List<CaseCategory> caseCategoriesOptions = new ArrayList<>();

    private CaseCategory caseCategory;

    private List<SubjectCategory> subjectCategoriesOptions = new ArrayList<>();

    private SubjectCategory subjectCategory;

    private FilterView filterView;

    private List<MediaView> mediaTypes;

    private List<LanguageView> languages;

    private List<ConnotationView> connotations;

    private boolean enableAddAdmUnit = true;

    private ListModelList<Thematic> thematicsOptions = new ListModelList<>();

    private ListModelList<Series> serieOptions = new ListModelList<>();

    private ListModelList<SubSeries> subSerieOptions = new ListModelList<>();

    private List<CategoryGroup> categoryGroupOptions = new ArrayList<>();

    private AutocompleteListModel<AdminUnit> adminUnits;

    private Set<WordFilterType> wordFilterTypeOptions;

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

    @WireVariable
    private AutocompleteService<AdminUnit> adminUnitsAutocompleteService;

    @WireVariable
    private FilterNormalizer filterNormalizer;

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
        categoryGroupOptions.clear();
        wordFilterTypeOptions = Arrays.stream(WordFilterType.values()).collect(Collectors.toSet());
        initMediaTypesOptions();
        initLanguages();
        initConnotations();
        this.adminUnits = new AutocompleteListModel<AdminUnit>(adminUnitsAutocompleteService,
                userSessionService.getLanguage());
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
        CasesFilterRequest filterRequest = new CasesFilterRequest();
        PageRequest pageRequest = new PageRequest(activePage, pageSize, sortField);
        pageRequest.setSortOrder(this.sortOrder);
        filterRequest.setPageRequest(pageRequest);
        filterRequest.setFilterActive(filterActive);
        setFilters(filterRequest);
        setAdvancedFilters(filterRequest);

        PageResponse<CaseListView> pageResponse = caseService.findAll(filterRequest);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.cases.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    @Command
    @NotifyChange({ "cases", "moreResults", "filter", "filterActive" })
    public void search() {
        this.filterActive = true;
        this.setDefaultPage();
        this.cases.clear();
        this.findCases();
    }

    @Command
    public void download() {
        CasesFilterRequest filterRequest = new CasesFilterRequest();
        filterRequest.setFilterActive(filterActive);
        setFilters(filterRequest);
        setAdvancedFilters(filterRequest);

        byte[] data = caseService.getReport(filterRequest, userSessionService.getLanguage(),
                userSessionService.getTimeZone());
        Filedownload.save(data, "application/vnd.ms-excel", "file.xls");
    }

    @Command
    @NotifyChange({ "serieOptions", "categoryGroupOptions", "subSerieOptions" })
    public void selectThematic(@BindingParam("fxFilter") FilterView fxFilter) {
        serieOptions.clear();
        if (fxFilter.getThematic() != null && fxFilter.getThematic().getId() != null && serieOptions.isEmpty()) {
            serieOptions.add(new Series());
            serieOptions.addAll(fxFilter.getThematic().getSeries());
        }
        fxFilter.setSerie(null);
        categoryGroupOptions.clear();
        if (fxFilter.getSerie() != null && fxFilter.getSerie().getId() != null && categoryGroupOptions.isEmpty()) {
            categoryGroupOptions.addAll(thematicService.findCategoriesBySerieId(fxFilter.getThematic().getId(),
                    fxFilter.getSerie().getId()));
        }
        fxFilter.getFilterCategories().clear();
        BindUtils.postNotifyChange(null, null, fxFilter, "subSerie");
        BindUtils.postNotifyChange(null, null, fxFilter, "filterCategories");
    }

    @Command
    @NotifyChange({ "subSerieOptions", "categoryGroupOptions" })
    public void selectSerie(@BindingParam("fxFilter") FilterView fxFilter) {
        subSerieOptions.clear();
        if (fxFilter.getSerie() != null && fxFilter.getSerie().getId() != null && subSerieOptions.isEmpty()) {
            subSerieOptions.add(new SubSeries());
            subSerieOptions.addAll(fxFilter.getSerie().getSubSeries());
        }
        fxFilter.setSubSerie(null);
        categoryGroupOptions.clear();
        if (fxFilter.getSerie() != null && fxFilter.getSerie().getId() != null && categoryGroupOptions.isEmpty()) {
            categoryGroupOptions.addAll(thematicService.findCategoriesBySerieId(fxFilter.getThematic().getId(),
                    fxFilter.getSerie().getId()));
        }
        BindUtils.postNotifyChange(null, null, fxFilter, "subSerie");
        BindUtils.postNotifyChange(null, null, fxFilter, "filterCategories");
    }

    @Command
    public void addFilterWord(@BindingParam("fxFilter") FilterView fxFilter) {
        fxFilter.getFilterWords().add(new WordFilter());
        BindUtils.postNotifyChange(null, null, fxFilter, "filterWords");
    }

    @Command
    @NotifyChange("enableAddAdmUnit")
    public void addFilterAdmUnit(@BindingParam("fxFilter") FilterView fxFilter) {
        AdmUnitFilterView admUnitFilter = new AdmUnitFilterView();
        admUnitFilter.setEditingStatus(true);
        fxFilter.getAdmUnitFilters().add(admUnitFilter);
        this.enableAddAdmUnit = false;
        BindUtils.postNotifyChange(null, null, fxFilter, "admUnitFilters");
    }

    @Command
    @NotifyChange("enableAddAdmUnit")
    public void confirmAdmUnit(@BindingParam("index") int idx, @BindingParam("fxFilter") FilterView fxFilter) {
        AdmUnitFilterView admUnit = fxFilter.getAdmUnitFilters().get(idx);
        admUnit.setEditingStatus(Boolean.FALSE);
        this.enableAddAdmUnit = true;
        BindUtils.postNotifyChange(null, null, fxFilter, "admUnitFilters");
    }

    @Command
    @NotifyChange("enableAddAdmUnit")
    public void removeAdmUnit(@BindingParam("index") int idx, @BindingParam("fxFilter") FilterView fxFilter) {
        fxFilter.getAdmUnitFilters().remove(idx);
        this.enableAddAdmUnit = true;
        BindUtils.postNotifyChange(null, null, fxFilter, "admUnitFilters");
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

    @Command
    public void selectGroupCategory(@BindingParam("filter") CategoryFilterView filterCategory) {
        filterCategory.getCategoryOptions().clear();
        filterCategory.setCategories(null);
        if (filterCategory.getCategoryGroup() != null && filterCategory.getCategoryOptions().isEmpty()) {
            filterCategory.getCategoryOptions().addAll(filterCategory.getCategoryGroup().getCategorias());
        }
        Map<String, Object> args = new HashMap<>();
        args.put("filterCategory", filterCategory);
        Executions.createComponents("/pages/triggers/create/choose-categories.zul", null, args);
        BindUtils.postNotifyChange(null, null, filterCategory, "categories");
    }

    @Command
    public String createCategoryName(NameByLanguage category) {
        String language = userSessionService.getLanguage();
        return category.getNameByLanguage(language);
    }

    @Command
    public void addFilterCategory(@BindingParam("fxFilter") FilterView fxFilter) {
        fxFilter.getFilterCategories().add(new CategoryFilterView());
        BindUtils.postNotifyChange(null, null, fxFilter, "filterCategories");
    }

    @Command
    public void removeFilterCategory(@BindingParam("fxFilter") FilterView fxFilter,
            @BindingParam("filter") CategoryFilterView filter) {
        fxFilter.getFilterCategories().remove(filter);
        BindUtils.postNotifyChange(null, null, fxFilter, "filterCategories");
    }

    @Command
    public void removeCategory(@BindingParam("filter") CategoryFilterView filter,
            @BindingParam("category") Category category) {
        filter.getCategories().remove(category);
        BindUtils.postNotifyChange(null, null, filter, "categories");
    }

    @Command
    public void editCategories(@BindingParam("filter") CategoryFilterView filter,
            @BindingParam("category") Category category) {
        Map<String, Object> args = new HashMap<>();
        args.put("filterCategory", filter);
        Executions.createComponents("/pages/triggers/create/choose-categories.zul", null, args);
        BindUtils.postNotifyChange(null, null, filter, "categories");
    }

    @Command
    public void removeFilterWord(@BindingParam("fxFilter") FilterView fxFilter,
            @BindingParam("filter") WordFilter filter) {
        fxFilter.getFilterWords().remove(filter);
        BindUtils.postNotifyChange(null, null, fxFilter, "filterWords");
    }

    @Command
    public void initFilterEndTime(@BindingParam("fxFilter") FilterView fxFilter) {
        if (fxFilter.getEndTime() == null) {
            TimeZone timeZone = getTimeZone();
            fxFilter.setEndTime(DateTimeBoxComponent.truncateTimeToday(timeZone));
            BindUtils.postNotifyChange(null, null, fxFilter, "endTime");
        }
    }

    @Command
    public void initFilterStartTime(@BindingParam("fxFilter") FilterView fxFilter) {
        if (fxFilter.getStartTime() == null) {
            TimeZone timeZone = getTimeZone();
            fxFilter.setStartTime(DateTimeBoxComponent.truncateTimeToday(timeZone));
            BindUtils.postNotifyChange(null, null, fxFilter, "startTime");
        }
    }

    private void addAdmUnitText(StringBuilder sb, BaseAdminUnit adminUnit) {
        sb.append(adminUnit.getTranslation());
        sb.append("(");
        sb.append(Labels.getLabel("trigger.criteria.admUnit.value." + adminUnit.getType().name()));
        sb.append(")");
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

        if (pendingResponse != null && !pendingResponse.equals(ALL_OPTION_VALUE)) {
            filterRequest.setPendingResponse(this.pendingResponse);
        }

        if (userSelected != null && !userSelected.getId().equals(NOT_ID_INT_VALUE)) {
            filterRequest.setUserSelected(this.userSelected.getUsername());
        }

        if (status != null && !status.equals(ALL_OPTION_VALUE)) {
            filterRequest.setStatus(this.status);
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

    private void setAdvancedFilters(CasesFilterRequest filterRequest) {
        if (this.caseCategory != null && !this.caseCategory.getId().equals(NOT_ID_ITEM_VALUE)) {
            filterRequest.setCaseCategory(this.caseCategory.getId());
        }

        if (this.subjectCategory != null && !this.subjectCategory.getId().equals(NOT_ID_ITEM_VALUE)) {
            filterRequest.setSubjectCategory(this.subjectCategory.getId());
        }
        addMediaFilter(filterRequest);
        addLanguageFilter(filterRequest);
        addDateRangeFilter(filterRequest);
        addConnotationFilter(filterRequest);
        addFollowersFilter(filterRequest);
        addWordFilters(filterRequest);
        addSerieFilters(filterRequest);
        addCategoriesFilters(filterRequest);
        addAdmUnitFilters(filterRequest);

    }

    private void addMediaFilter(CasesFilterRequest request) {
        List<MediaView> mediasPicked = mediaTypes.stream().filter(media -> media.isChecked())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(mediasPicked) && mediasPicked.size() < mediaTypes.size()) {
            Integer[] options = mediasPicked.stream().map(media -> media.getMedia().getValue().intValue())
                    .toArray(size -> new Integer[size]);
            request.setMediums(options);
        }
    }

    private void addLanguageFilter(CasesFilterRequest request) {
        List<LanguageView> languagesPicked = languages.stream().filter(language -> language.isChecked())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(languagesPicked) && languagesPicked.size() < languages.size()) {
            String[] options = languagesPicked.stream().map(language -> language.getLanguage().getValue())
                    .toArray(size -> new String[size]);
            request.setLanguages(options);
        }
    }

    private void addDateRangeFilter(CasesFilterRequest request) {
        if (filterView.getStartDateTime() != null || filterView.getEndDateTime() != null) {
            TimeZone timeZone = getTimeZone();
            if (filterView.getStartDateTime() != null) {
                request.setTimeFrom(DateTimeBoxComponent.mergeDate(filterView.getStartDateTime(),
                        filterView.getStartTime(), timeZone));
            }
            if (filterView.getEndDateTime() != null) {
                request.setTimeTo(DateTimeBoxComponent.mergeDate(filterView.getEndDateTime(), filterView.getEndTime(),
                        timeZone));
            }
        }
    }

    private TimeZone getTimeZone() {
        return (TimeZone) Executions.getCurrent().getSession().getAttribute(Attributes.PREFERRED_TIME_ZONE);
    }

    private void addConnotationFilter(CasesFilterRequest request) {
        List<ConnotationView> connotationsPicked = connotations.stream().filter(connotation -> connotation.isChecked())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(connotationsPicked) && connotationsPicked.size() < connotations.size()) {
            String[] options = connotationsPicked.stream()
                    .map(connotation -> String.valueOf(connotation.getConnotation().getValue()))
                    .toArray(size -> new String[size]);
            request.setConnotations(options);
        }
    }

    private void addFollowersFilter(CasesFilterRequest request) {
        if (filterView.getFollowersGreaterThan() != null || filterView.getFollowersLessThan() != null) {
            RangeRequest rangeRequest = new RangeRequest();
            FollowersCountRange followersCount = new FollowersCountRange();
            followersCount.setGt(filterView.getFollowersGreaterThan());
            followersCount.setLt(filterView.getFollowersLessThan());
            rangeRequest.setFollowersCount(followersCount);
            request.setRange(rangeRequest);
        }
    }

    private void addWordFilters(CasesFilterRequest request) {
        if (CollectionUtils.isNotEmpty(filterView.getFilterWords())) {
            WordsFilterRequestBean cloudsurfer = new WordsFilterRequestBean();
            WordsListFilterBean[] wordList = filterView.getFilterWords().stream().map(filterWord -> {
                WordsListFilterBean wordFilter = new WordsListFilterBean();
                wordFilter.setPalabra(filterWord.getInputText());
                wordFilter.setTipo(filterWord.getType().getName());
                return wordFilter;
            }).toArray(size -> new WordsListFilterBean[size]);
            cloudsurfer.setWordList(wordList);
            request.setCloudsurfer(cloudsurfer);
        }
    }

    private void addSerieFilters(CasesFilterRequest request) {
        if (filterView.getThematic() != null && filterView.getThematic().getId() != null) {
            request.setTokenId(filterView.getThematic().getId());
            // request.setIdRealTimeProduct(filterView.getThematic().getNombre());
            if (filterView.getSerie() != null) {
                request.setSerieId(filterView.getSerie().getId());
                request.setSerieName(filterView.getSerie().getNombre());
            }
            if (filterView.getSubSerie() != null) {
                request.setSubSerieId(filterView.getSubSerie().getId());
                request.setSubSerieName(filterView.getSubSerie().getNombre());
            } else {
                request.setSubSeriesDefined(filterView.getSerie().getSubSeries().stream().map(SubSeries::getId)
                        .toArray(size -> new Long[size]));
            }
        }
    }

    private void addCategoriesFilters(CasesFilterRequest request) {
        if (CollectionUtils.isNotEmpty(filterView.getFilterCategories())) {
            Long[] categories = filterView.getFilterCategories().stream()
                    .map(filterCategory -> filterCategory.getCategories()).flatMap(list -> list.stream())
                    .map(category -> category.getId()).toArray(size -> new Long[size]);
            request.setCategories(categories);
            request.setCategoriesFilter(filterView.getFilterCategories().stream()
                    .map(filterCategory -> filterCategory.getCategories()).flatMap(list -> list.stream())
                    .collect(Collectors.toList()));
        }
    }

    private void addAdmUnitFilters(CasesFilterRequest request) {
        if (CollectionUtils.isNotEmpty(filterView.getAdmUnitFilters())) {
            AdministrativeUnitsFilterBean[] administrativeUnitsFilterBeans = filterView
                    .getAdmUnitFilters()
                    .stream()
                    .map(admUnitFilter -> {
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
                        return administrativeUnitsFilterBean;
                    }).toArray(size -> new AdministrativeUnitsFilterBean[size]);
            AdministrativeUnitsFilter administrativeUnitsFilter = new AdministrativeUnitsFilter();
            administrativeUnitsFilter.setAdministrativeUnitsFilterBeans(administrativeUnitsFilterBeans);
            request.setAdministrativeUnitsFilter(administrativeUnitsFilter);
        }

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

    private void initMediaTypesOptions() {
        mediaTypes = new ArrayList<>();
        for (Media media : Media.values()) {
            MediaView mediaView = new MediaView();
            mediaView.setMedia(media);
            mediaView.setChecked(false);
            mediaTypes.add(mediaView);
        }
    }

    private void initLanguages() {
        languages = new ArrayList<>();
        for (Language language : Language.values()) {
            LanguageView languageView = new LanguageView();
            languageView.setLanguage(language);
            languageView.setChecked(false);
            languages.add(languageView);
        }
    }

    private void initConnotations() {
        connotations = new ArrayList<>();
        for (Connotation connotation : Connotation.values()) {
            ConnotationView connotationView = new ConnotationView();
            connotationView.setConnotation(connotation);
            connotationView.setChecked(false);
            connotations.add(connotationView);
        }
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

    public List<CategoryGroup> getCategoryGroupOptions() {
        return categoryGroupOptions;
    }

    public FilterView getFilter() {
        return filterView;
    }

    public List<MediaView> getMediaTypes() {
        return mediaTypes;
    }

    public List<LanguageView> getLanguages() {
        return languages;
    }

    public List<ConnotationView> getConnotations() {
        return connotations;
    }

    public boolean isEnableAddAdmUnit() {
        return enableAddAdmUnit;
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

    public AutocompleteListModel<AdminUnit> getAdminUnits() {
        return adminUnits;
    }

    public Set<WordFilterType> getWordFilterTypeOptions() {
        return wordFilterTypeOptions;
    }

}
