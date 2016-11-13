package com.qsocialnow.viewmodel.retroactive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.util.resource.Labels;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.ListModelList;

import com.qsocialnow.common.model.config.AdminUnit;
import com.qsocialnow.common.model.config.BaseAdminUnit;
import com.qsocialnow.common.model.config.Category;
import com.qsocialnow.common.model.config.CategoryGroup;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.NameByLanguage;
import com.qsocialnow.common.model.config.Series;
import com.qsocialnow.common.model.config.SubSeries;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.common.model.config.WordFilter;
import com.qsocialnow.common.model.config.WordFilterType;
import com.qsocialnow.common.model.filter.AdministrativeUnitsFilter;
import com.qsocialnow.common.model.filter.AdministrativeUnitsFilterBean;
import com.qsocialnow.common.model.filter.FilterNormalizer;
import com.qsocialnow.common.model.filter.FollowersCountRange;
import com.qsocialnow.common.model.filter.RangeRequest;
import com.qsocialnow.common.model.filter.WordsFilterRequestBean;
import com.qsocialnow.common.model.filter.WordsListFilterBean;
import com.qsocialnow.common.model.retroactive.RetroactiveProcess;
import com.qsocialnow.common.model.retroactive.RetroactiveProcessRequest;
import com.qsocialnow.common.model.retroactive.RetroactiveProcessStatus;
import com.qsocialnow.converters.DateConverter;
import com.qsocialnow.model.AdmUnitFilterView;
import com.qsocialnow.model.CategoryFilterView;
import com.qsocialnow.model.Connotation;
import com.qsocialnow.model.ConnotationView;
import com.qsocialnow.model.FilterView;
import com.qsocialnow.model.Language;
import com.qsocialnow.model.LanguageView;
import com.qsocialnow.model.MediaView;
import com.qsocialnow.services.AutocompleteService;
import com.qsocialnow.services.RetroactiveService;
import com.qsocialnow.services.ThematicService;
import com.qsocialnow.services.UserSessionService;
import com.qsocialnow.util.DateTimeBoxComponent;
import com.qsocialnow.viewmodel.AutocompleteListModel;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "filter$updateEvent", onChange = "_vm_.filters")
@ToClientCommand("filter$updateEvent")
public class RetroactiveDashboardViewModel implements Serializable {

    private static final long serialVersionUID = -2827241577623957316L;

    @WireVariable
    private RetroactiveService retroactiveService;

    @WireVariable
    private ThematicService thematicService;

    @WireVariable
    private AutocompleteService<AdminUnit> adminUnitsAutocompleteService;

    @WireVariable
    private UserSessionService userSessionService;

    @WireVariable
    private FilterNormalizer filterNormalizer;

    private RetroactiveProcess currentProcess;

    private AutocompleteListModel<AdminUnit> adminUnits;

    private List<MediaView> mediaTypes;

    private List<ConnotationView> connotations;

    private List<LanguageView> languages;

    private FilterView filterView;

    private Set<WordFilterType> wordFilterTypeOptions;

    private ListModelList<Thematic> thematicsOptions = new ListModelList<>();

    private ListModelList<Series> serieOptions = new ListModelList<>();

    private ListModelList<SubSeries> subSerieOptions = new ListModelList<>();

    private List<CategoryGroup> categoryGroupOptions = new ArrayList<>();

    private boolean filters;

    private boolean enableAddAdmUnit = true;

    private DateConverter dateConverter;

    private boolean cancelling;

    private boolean running;

    private RetroactiveRequestView currentRequest;

    public List<MediaView> getMediaTypes() {
        return mediaTypes;
    }

    public FilterView getFilter() {
        return filterView;
    }

    public List<ConnotationView> getConnotations() {
        return connotations;
    }

    public List<LanguageView> getLanguages() {
        return languages;
    }

    public Set<WordFilterType> getWordFilterTypeOptions() {
        return wordFilterTypeOptions;
    }

    public List<Thematic> getThematicsOptions() {
        return thematicsOptions;
    }

    public boolean isFilters() {
        return filters;
    }

    public List<CategoryGroup> getCategoryGroupOptions() {
        return categoryGroupOptions;
    }

    public ListModelList<Series> getSerieOptions() {
        return serieOptions;
    }

    public List<SubSeries> getSubSerieOptions() {
        return subSerieOptions;
    }

    public AutocompleteListModel<AdminUnit> getAdminUnits() {
        return adminUnits;
    }

    public boolean isEnableAddAdmUnit() {
        return enableAddAdmUnit;
    }

    public RetroactiveProcess getCurrentProcess() {
        return currentProcess;
    }

    public DateConverter getDateConverter() {
        return dateConverter;
    }

    public boolean isCancelling() {
        return cancelling;
    }

    public boolean isRunning() {
        return running;
    }

    public RetroactiveRequestView getCurrentRequest() {
        return currentRequest;
    }

    @Init
    public void init() {
        currentProcess = retroactiveService.getCurrentProcess();
        initCurrentRequest();
        filterView = new FilterView();
        wordFilterTypeOptions = Arrays.stream(WordFilterType.values()).collect(Collectors.toSet());
        serieOptions.clear();
        subSerieOptions.clear();
        categoryGroupOptions.clear();
        initMedias();
        initConnotations();
        initLanguages();
        initThematics();
        this.adminUnits = new AutocompleteListModel<AdminUnit>(adminUnitsAutocompleteService,
                userSessionService.getLanguage());
        this.dateConverter = new DateConverter(userSessionService.getTimeZone());
        setRunning();
    }

    private void initCurrentRequest() {
        if (currentProcess.getProgress() != null) {
            currentRequest = new RetroactiveRequestView();
            Integer[] mediums = currentProcess.getRequest().getMediums();
            if (ArrayUtils.isNotEmpty(mediums)) {
                currentRequest.setMedias(Arrays.stream(mediums).map(medium -> Media.getByValue(medium.longValue()))
                        .collect(Collectors.toList()));
            }
            String[] languages = currentProcess.getRequest().getLanguages();
            if (ArrayUtils.isNotEmpty(languages)) {
                currentRequest.setLanguages(Arrays.stream(languages).map(language -> Language.getByValue(language))
                        .collect(Collectors.toList()));
            }
            currentRequest.setEventsFrom(currentProcess.getRequest().getTimeFrom());
            currentRequest.setEventsTo(currentProcess.getRequest().getTimeTo());
            String[] connotations = currentProcess.getRequest().getConnotations();
            if (ArrayUtils.isNotEmpty(connotations)) {
                currentRequest.setConnotations(Arrays.stream(connotations)
                        .map(connotation -> Connotation.getByName(connotation)).collect(Collectors.toList()));
            }
            RangeRequest range = currentProcess.getRequest().getRange();
            if (range != null) {
                currentRequest.setFollowers(range.getFollowersCount());
            }
            WordsFilterRequestBean cloudsurfer = currentProcess.getRequest().getCloudsurfer();
            if (cloudsurfer != null) {
                currentRequest.setWords(cloudsurfer.getWordList());
            }
            AdministrativeUnitsFilter administrativeUnitsFilter = currentProcess.getRequest()
                    .getAdministrativeUnitsFilter();
            if (administrativeUnitsFilter != null) {
                currentRequest.setAdministrativeUnits(administrativeUnitsFilter.getAdministrativeUnitsFilterBeans());
            }
            if (currentProcess.getRequest().getIdRealTimeProduct() != null) {
                currentRequest.setThematic(currentProcess.getRequest().getIdRealTimeProduct());
            }
            if (currentProcess.getRequest().getSerieName() != null) {
                currentRequest.setSerie(currentProcess.getRequest().getSerieName());
            }
            if (currentProcess.getRequest().getSubSerieName() != null) {
                currentRequest.setSubSerie(currentProcess.getRequest().getSubSerieName());
            }
            if (currentProcess.getRequest().getCategoriesFilter() != null) {
                currentRequest.setCategories(currentProcess.getRequest().getCategoriesFilter());
            }
        }

    }

    private void setRunning() {
        running = currentProcess.getProgress() != null
                && (RetroactiveProcessStatus.PROCESSING.equals(currentProcess.getProgress().getStatus()) || RetroactiveProcessStatus.START
                        .equals(currentProcess.getProgress().getStatus()));
        filters = !running;
    }

    public String getProgressLabel() {
        if (currentProcess.getProgress() != null) {
            return Labels.getLabel(String.format("retroactive.status.%s", currentProcess.getProgress().getStatus()),
                    new Long[] { currentProcess.getProgress().getEventsProcessed() });
        }
        return null;
    }

    @Command
    @NotifyChange({ "currentProcess", "progressLabel", "cancelling", "running", "filters", "currentRequest" })
    public void refresh() {
        currentProcess = retroactiveService.getCurrentProcess();
        initCurrentRequest();
        setRunning();
        if (cancelling) {
            cancelling = !RetroactiveProcessStatus.STOP.equals(currentProcess.getProgress().getStatus());
        }
    }

    @Command
    @NotifyChange({ "currentProcess", "progressLabel", "running", "filters", "filter", "connotations", "mediaTypes",
            "languages", "serieOptions", "subSerieOptions", "categoryGroupOptions", "currentRequest" })
    public void save() {
        RetroactiveProcessRequest request = new RetroactiveProcessRequest();
        addMediaFilter(request);
        addLanguageFilter(request);
        addDateRangeFilter(request);
        addConnotationFilter(request);
        addFollowersFilter(request);
        addWordFilters(request);
        addSerieFilters(request);
        addCategoriesFilters(request);
        addAdmUnitFilters(request);
        request.setTimezone(userSessionService.getTimeZone());
        request.setLanguage(userSessionService.getLanguage());
        request.setCreatedDate(new Date().getTime());
        request.setUsername(userSessionService.getUsername());
        retroactiveService.executeNewProcess(request);
        init();
    }

    @Command
    @NotifyChange("cancelling")
    public void cancel() {
        retroactiveService.cancelProcess();
        cancelling = true;
    }

    private void initThematics() {
        if (thematicsOptions.isEmpty()) {
            thematicsOptions.addAll(thematicService.findAll());
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

    private void initMedias() {
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

    private void addAdmUnitText(StringBuilder sb, BaseAdminUnit adminUnit) {
        sb.append(adminUnit.getTranslation());
        sb.append("(");
        sb.append(Labels.getLabel("trigger.criteria.admUnit.value." + adminUnit.getType().name()));
        sb.append(")");
    }

    @Command
    public void removeFilterWord(@BindingParam("fxFilter") FilterView fxFilter,
            @BindingParam("filter") WordFilter filter) {
        fxFilter.getFilterWords().remove(filter);
        BindUtils.postNotifyChange(null, null, fxFilter, "filterWords");
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
    @NotifyChange({ "serieOptions", "categoryGroupOptions", "subSerieOptions" })
    public void selectThematic(@BindingParam("fxFilter") FilterView fxFilter) {
        serieOptions.clear();
        if (fxFilter.getThematic() != null && fxFilter.getThematic().getId() != null && serieOptions.isEmpty()) {
            serieOptions.addAll(fxFilter.getThematic().getSeries());
        }
        fxFilter.setSerie(null);
        selectSerie(fxFilter);
        BindUtils.postNotifyChange(null, null, fxFilter, "serie");
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
        fxFilter.getFilterCategories().clear();
        BindUtils.postNotifyChange(null, null, fxFilter, "subSerie");
        BindUtils.postNotifyChange(null, null, fxFilter, "filterCategories");
    }

    @Command
    public String createCategoryName(NameByLanguage category) {
        String language = userSessionService.getLanguage();
        return category.getNameByLanguage(language);
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

    @GlobalCommand
    public void closeCategories() {
    }

    private void addMediaFilter(RetroactiveProcessRequest request) {
        List<MediaView> mediasPicked = mediaTypes.stream().filter(media -> media.isChecked())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(mediasPicked) && mediasPicked.size() < mediaTypes.size()) {
            Integer[] options = mediasPicked.stream().map(media -> media.getMedia().getValue().intValue())
                    .toArray(size -> new Integer[size]);
            request.setMediums(options);
        }
    }

    private void addLanguageFilter(RetroactiveProcessRequest request) {
        List<LanguageView> languagesPicked = languages.stream().filter(language -> language.isChecked())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(languagesPicked) && languagesPicked.size() < languages.size()) {
            String[] options = languagesPicked.stream().map(language -> language.getLanguage().getValue())
                    .toArray(size -> new String[size]);
            request.setLanguages(options);
        }
    }

    private void addDateRangeFilter(RetroactiveProcessRequest request) {
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

    private void addConnotationFilter(RetroactiveProcessRequest request) {
        List<ConnotationView> connotationsPicked = connotations.stream().filter(connotation -> connotation.isChecked())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(connotationsPicked) && connotationsPicked.size() < connotations.size()) {
            String[] options = connotationsPicked.stream().map(connotation -> connotation.getConnotation().getName())
                    .toArray(size -> new String[size]);
            request.setConnotations(options);
        }
    }

    private void addFollowersFilter(RetroactiveProcessRequest request) {
        if (filterView.getFollowersGreaterThan() != null || filterView.getFollowersLessThan() != null) {
            RangeRequest rangeRequest = new RangeRequest();
            FollowersCountRange followersCount = new FollowersCountRange();
            followersCount.setGt(filterView.getFollowersGreaterThan());
            followersCount.setLt(filterView.getFollowersLessThan());
            rangeRequest.setFollowersCount(followersCount);
            request.setRange(rangeRequest);
        }

    }

    private void addWordFilters(RetroactiveProcessRequest request) {
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

    private void addSerieFilters(RetroactiveProcessRequest request) {
        if (filterView.getThematic() != null && filterView.getThematic().getId() != null) {
            request.setTokenId(filterView.getThematic().getId());
            request.setIdRealTimeProduct(filterView.getThematic().getNombre());
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

    private void addCategoriesFilters(RetroactiveProcessRequest request) {
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

    private void addAdmUnitFilters(RetroactiveProcessRequest request) {
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

    private TimeZone getTimeZone() {
        return (TimeZone) Executions.getCurrent().getSession().getAttribute(Attributes.PREFERRED_TIME_ZONE);
    }

}
