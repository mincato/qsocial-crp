package com.qsocialnow.viewmodel.trigger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.ListModelList;

import com.qsocialnow.common.model.config.AutomaticActionCriteria;
import com.qsocialnow.common.model.config.Category;
import com.qsocialnow.common.model.config.CategoryFilter;
import com.qsocialnow.common.model.config.CategoryGroup;
import com.qsocialnow.common.model.config.ConnotationFilter;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.FollowersFilter;
import com.qsocialnow.common.model.config.LanguageFilter;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.MediaFilter;
import com.qsocialnow.common.model.config.PeriodFilter;
import com.qsocialnow.common.model.config.SerieFilter;
import com.qsocialnow.common.model.config.Series;
import com.qsocialnow.common.model.config.SubSeries;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.common.model.config.WordFilter;
import com.qsocialnow.common.model.config.WordFilterType;
import com.qsocialnow.model.CategoryFilterView;
import com.qsocialnow.model.Connotation;
import com.qsocialnow.model.ConnotationView;
import com.qsocialnow.model.FilterView;
import com.qsocialnow.model.Language;
import com.qsocialnow.model.LanguageView;
import com.qsocialnow.model.MediaView;
import com.qsocialnow.model.SegmentView;
import com.qsocialnow.model.TriggerView;
import com.qsocialnow.services.ThematicService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "filter$updateEvent", onChange = "_vm_.filters")
@ToClientCommand("filter$updateEvent")
public class CreateCriteriaViewModel implements Serializable {

    private static final long serialVersionUID = -4119198423406156946L;

    @WireVariable
    private ThematicService thematicService;

    private DetectionCriteria currentCriteria;

    private List<MediaView> mediaTypes;

    private List<ConnotationView> connotations;

    private List<LanguageView> languages;

    private FilterView filterView;

    private Set<WordFilterType> wordFilterTypeOptions;

    private ListModelList<Thematic> thematicsOptions = new ListModelList<>();

    private ListModelList<Series> serieOptions = new ListModelList<>();

    private ListModelList<SubSeries> subSerieOptions = new ListModelList<>();

    private List<CategoryGroup> categoryGroupOptions = new ArrayList<>();

    private boolean editing;

    private boolean filters;

    private DetectionCriteria fxCriteria;

    private TriggerView trigger;

    private SegmentView segment;

    public DetectionCriteria getCurrentCriteria() {
        return currentCriteria;
    }

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

    @Init
    public void init() {
        currentCriteria = new DetectionCriteria();
        currentCriteria.setActionCriterias(new ArrayList<>());
        filterView = new FilterView();
        wordFilterTypeOptions = Arrays.stream(WordFilterType.values()).collect(Collectors.toSet());
        initMedias(null);
        initConnotations(null);
        initLanguages(null);

    }

    @Command
    public void save() {
        Filter filter = new Filter();
        addMediaFilter(filter);
        addLanguageFilter(filter);
        addDateRangeFilter(filter);
        addConnotationFilter(filter);
        addFollowersFilter(filter);
        addWordFilters(filter);
        addSerieFilters(filter);
        addCategoriesFilters(filter);
        currentCriteria.setFilter(filter);
        if (editing) {
            BindUtils.postGlobalCommand(null, null, "updateCriteria", new HashMap<>());
        } else {
            Map<String, Object> args = new HashMap<>();
            args.put("detectionCriteria", currentCriteria);
            BindUtils.postGlobalCommand(null, null, "addCriteria", args);
        }
    }

    @Command
    public void cancel() {
        BindUtils.postGlobalCommand(null, null, "goToSegment", new HashMap<>());
    }

    @GlobalCommand
    @NotifyChange({ "currentCriteria", "filters", "filter", "connotations", "mediaTypes", "languages", "serieOptions",
            "subSerieOptions", "categoryGroupOptions" })
    public void initCriteria(@BindingParam("currentDomain") Domain currentDomain,
            @BindingParam("trigger") TriggerView trigger, @BindingParam("segment") SegmentView segment) {
        currentCriteria = new DetectionCriteria();
        currentCriteria.setActionCriterias(new ArrayList<>());
        filterView = new FilterView();
        serieOptions.clear();
        subSerieOptions.clear();
        categoryGroupOptions.clear();
        initThematics(currentDomain);
        initMedias(null);
        initConnotations(null);
        initLanguages(null);
        editing = false;
        filters = true;
        this.trigger = trigger;
        this.segment = segment;

    }

    @GlobalCommand
    @NotifyChange({ "currentCriteria", "filters", "filter", "connotations", "mediaTypes", "languages", "serieOptions",
            "subSerieOptions", "categoryGroupOptions" })
    public void editCriteria(@BindingParam("currentDomain") Domain currentDomain,
            @BindingParam("trigger") TriggerView trigger, @BindingParam("segment") SegmentView segment,
            @BindingParam("detectionCriteria") DetectionCriteria detectionCriteria) {
        currentCriteria = detectionCriteria;
        if (currentCriteria.getActionCriterias() == null) {
            currentCriteria.setActionCriterias(new ArrayList<>());
        }
        filterView = new FilterView();
        initThematics(currentDomain);
        initMedias(detectionCriteria);
        initConnotations(detectionCriteria);
        initLanguages(detectionCriteria);
        createFilter(detectionCriteria);
        editing = true;
        filters = true;
        this.trigger = trigger;
        this.segment = segment;
    }

    private void initThematics(Domain domain) {
        if (thematicsOptions.isEmpty()) {
            List<Thematic> allThematics = thematicService.findAll();
            Stream<Thematic> thematics = allThematics.stream().filter(
                    thematic -> (domain.getThematics().contains(thematic.getId())));
            thematicsOptions.addAll(thematics.collect(Collectors.toList()));
        }
    }

    private void initConnotations(DetectionCriteria detectionCriteria) {
        boolean containsFilter = detectionCriteria != null && detectionCriteria.getFilter() != null
                && detectionCriteria.getFilter().getConnotationFilter() != null;
        connotations = new ArrayList<>();
        for (Connotation connotation : Connotation.values()) {
            ConnotationView connotationView = new ConnotationView();
            connotationView.setConnotation(connotation);
            connotationView.setChecked(containsFilter
                    && ArrayUtils.contains(detectionCriteria.getFilter().getConnotationFilter().getOptions(),
                            connotation.getValue()));
            connotations.add(connotationView);
        }
    }

    private void initMedias(DetectionCriteria detectionCriteria) {
        boolean containsFilter = detectionCriteria != null && detectionCriteria.getFilter() != null
                && detectionCriteria.getFilter().getMediaFilter() != null;
        mediaTypes = new ArrayList<>();
        for (Media media : Media.values()) {
            MediaView mediaView = new MediaView();
            mediaView.setMedia(media);
            mediaView.setChecked(containsFilter
                    && ArrayUtils.contains(detectionCriteria.getFilter().getMediaFilter().getOptions(),
                            media.getValue()));
            mediaTypes.add(mediaView);
        }
    }

    private void initLanguages(DetectionCriteria detectionCriteria) {
        boolean containsFilter = detectionCriteria != null && detectionCriteria.getFilter() != null
                && detectionCriteria.getFilter().getLanguageFilter() != null;
        languages = new ArrayList<>();
        for (Language language : Language.values()) {
            LanguageView languageView = new LanguageView();
            languageView.setLanguage(language);
            languageView.setChecked(containsFilter
                    && ArrayUtils.contains(detectionCriteria.getFilter().getLanguageFilter().getOptions(),
                            language.getValue()));
            languages.add(languageView);
        }
    }

    @Command
    public void addFilterWord(@BindingParam("fxFilter") FilterView fxFilter) {
        fxFilter.getFilterWords().add(new WordFilter());
        BindUtils.postNotifyChange(null, null, fxFilter, "filterWords");
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
    @NotifyChange({ "serieOptions", "categoryGroupOptions", "subSerieOptions" })
    public void selectThematic(@BindingParam("fxFilter") FilterView fxFilter) {
        serieOptions.clear();
        if (fxFilter.getThematic() != null && serieOptions.isEmpty()) {
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
        if (fxFilter.getSerie() != null && subSerieOptions.isEmpty()) {
            subSerieOptions.addAll(fxFilter.getSerie().getSubSeries());
        }
        fxFilter.setSubSerie(null);
        categoryGroupOptions.clear();
        if (fxFilter.getSerie() != null && categoryGroupOptions.isEmpty()) {
            categoryGroupOptions.addAll(thematicService.findCategoriesBySerieId(fxFilter.getThematic().getId(),
                    fxFilter.getSerie().getId()));
        }
        fxFilter.getFilterCategories().clear();
        BindUtils.postNotifyChange(null, null, fxFilter, "subSerie");
        BindUtils.postNotifyChange(null, null, fxFilter, "filterCategories");
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
    }

    @GlobalCommand
    public void closeCategories() {
    }

    @Command
    public void createNewAction(@BindingParam("fxCriteria") DetectionCriteria fxCriteria) {
        this.fxCriteria = fxCriteria;
        Map<String, Object> args = new HashMap<>();
        args.put("currentDomain", null);
        args.put("trigger", trigger);
        args.put("segment", segment);
        BindUtils.postGlobalCommand(null, null, "goToAction", new HashMap<>());
        BindUtils.postGlobalCommand(null, null, "initAction", args);
    }

    @GlobalCommand
    public void addActionCriteria(@BindingParam("actionCriteria") AutomaticActionCriteria actionCriteria) {
        this.fxCriteria.getActionCriterias().add(actionCriteria);
        BindUtils.postGlobalCommand(null, null, "goToCriteria", new HashMap<>());
        BindUtils.postNotifyChange(null, null, this.fxCriteria, "actionCriterias");
        this.fxCriteria = null;
    }

    @Command
    public void removeAction(@BindingParam("fxCriteria") DetectionCriteria fxCriteria,
            @BindingParam("action") AutomaticActionCriteria actionCriteria) {
        fxCriteria.getActionCriterias().remove(actionCriteria);
        BindUtils.postNotifyChange(null, null, fxCriteria, "actionCriterias");
    }

    private void createFilter(DetectionCriteria detectionCriteria) {
        fillDateRangeFilter(filterView, detectionCriteria.getFilter().getPeriodFilter());
        fillFollowersFilter(filterView, detectionCriteria.getFilter().getFollowersFilter());
        fillWordFilters(filterView, detectionCriteria.getFilter().getWordFilters());
        fillSerieFilters(filterView, detectionCriteria.getFilter().getSerieFilter());
        fillCategoriesFilters(filterView, detectionCriteria.getFilter().getCategoryFilter());
    }

    private void fillCategoriesFilters(FilterView filterView, List<CategoryFilter> categoriesFilter) {
        if (CollectionUtils.isNotEmpty(categoriesFilter)) {
            List<CategoryFilterView> filterCategories = categoriesFilter
                    .stream()
                    .map(categoryFilter -> {
                        List<CategoryGroup> categoryGroupOptions = getCategoryGroupOptions();
                        CategoryFilterView categoryFilterView = new CategoryFilterView();
                        categoryFilterView.setCategoryGroup(categoryGroupOptions
                                .stream()
                                .filter(categoryGroup -> categoryGroup.getId()
                                        .equals(categoryFilter.getCategoryGroup())).findFirst().get());
                        categoryFilterView.getCategoryOptions().addAll(
                                categoryFilterView.getCategoryGroup().getCategorias());
                        if (ArrayUtils.isNotEmpty(categoryFilter.getCategories())) {
                            List<Category> categories = Arrays.stream(categoryFilter.getCategories()).map(category -> {
                                return categoryFilterView.getCategoryOptions().stream().filter(categoryOption -> {
                                    return categoryOption.getId().equals(category);
                                }).findFirst().get();
                            }).collect(Collectors.toList());
                            categoryFilterView.setCategories(categories);
                        }
                        return categoryFilterView;
                    }).collect(Collectors.toList());
            filterView.setFilterCategories(filterCategories);
        }
    }

    private void fillSerieFilters(FilterView filterView, SerieFilter serieFilter) {
        if (serieFilter != null) {
            if (serieFilter.getThematicId() != null) {
                filterView.setThematic(thematicsOptions.stream()
                        .filter(thematic -> thematic.getId().equals(serieFilter.getThematicId())).findFirst().get());
                if (filterView.getThematic() != null && serieOptions.isEmpty()) {
                    serieOptions.addAll(filterView.getThematic().getSeries());
                }
            }
            if (serieFilter.getSerieId() != null) {
                filterView.setSerie(getSerieOptions().stream()
                        .filter(serie -> serie.getId().equals(serieFilter.getSerieId())).findFirst().get());
                if (filterView.getSerie() != null && subSerieOptions.isEmpty()) {
                    subSerieOptions.addAll(filterView.getSerie().getSubSeries());
                }
                if (filterView.getSerie() != null && categoryGroupOptions.isEmpty()) {
                    categoryGroupOptions.addAll(thematicService.findCategoriesBySerieId(filterView.getThematic()
                            .getId(), filterView.getSerie().getId()));
                }
            }
            if (serieFilter.getSubSerieId() != null) {
                filterView.setSubSerie(getSubSerieOptions().stream()
                        .filter(serie -> serie.getId().equals(serieFilter.getSubSerieId())).findFirst().get());
            }
        }
    }

    private void fillWordFilters(FilterView filterView, List<WordFilter> wordFilters) {
        if (CollectionUtils.isNotEmpty(wordFilters)) {
            filterView.setFilterWords(wordFilters);
        }
    }

    private void fillFollowersFilter(FilterView filterView, FollowersFilter followersFilter) {
        if (followersFilter != null) {
            filterView.setFollowersGreaterThan(followersFilter.getMinFollowers());
            filterView.setFollowersLessThan(followersFilter.getMaxFollowers());
        }
    }

    private void fillDateRangeFilter(FilterView filterView, PeriodFilter periodFilter) {
        if (periodFilter != null) {
            if (periodFilter.getStartDateTime() != null) {
                filterView.setStartDateTime(new Date(periodFilter.getStartDateTime()));
            }
            if (periodFilter.getEndDateTime() != null) {
                filterView.setEndDateTime(new Date(periodFilter.getEndDateTime()));
            }
        }
    }

    private void addMediaFilter(Filter filter) {
        List<MediaView> mediasPicked = mediaTypes.stream().filter(media -> media.isChecked())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(mediasPicked) && mediasPicked.size() < mediaTypes.size()) {
            MediaFilter mediaFilter = new MediaFilter();
            Long[] options = mediasPicked.stream().map(media -> media.getMedia().getValue())
                    .toArray(size -> new Long[size]);
            mediaFilter.setOptions(options);
            filter.setMediaFilter(mediaFilter);
        }
    }

    private void addLanguageFilter(Filter filter) {
        List<LanguageView> languagesPicked = languages.stream().filter(language -> language.isChecked())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(languagesPicked) && languagesPicked.size() < languages.size()) {
            LanguageFilter languageFilter = new LanguageFilter();
            String[] options = languagesPicked.stream().map(language -> language.getLanguage().getValue())
                    .toArray(size -> new String[size]);
            languageFilter.setOptions(options);
            filter.setLanguageFilter(languageFilter);
        }
    }

    private void addDateRangeFilter(Filter filter) {
        if (filterView.getStartDateTime() != null || filterView.getEndDateTime() != null) {
            PeriodFilter periodFilter = new PeriodFilter();
            if (filterView.getStartDateTime() != null) {
                periodFilter.setStartDateTime(filterView.getStartDateTime().getTime());
            }
            if (filterView.getEndDateTime() != null) {
                periodFilter.setEndDateTime(filterView.getEndDateTime().getTime());
            }
            filter.setPeriodFilter(periodFilter);
        }

    }

    private void addConnotationFilter(Filter filter) {
        List<ConnotationView> connotationsPicked = connotations.stream().filter(connotation -> connotation.isChecked())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(connotationsPicked) && connotationsPicked.size() < connotations.size()) {
            ConnotationFilter connotationFilter = new ConnotationFilter();
            Short[] options = connotationsPicked.stream().map(connotation -> connotation.getConnotation().getValue())
                    .toArray(size -> new Short[size]);
            connotationFilter.setOptions(options);
            filter.setConnotationFilter(connotationFilter);
        }
    }

    private void addFollowersFilter(Filter filter) {
        if (filterView.getFollowersLessThan() != null || filterView.getFollowersLessThan() != null) {
            FollowersFilter followersFilter = new FollowersFilter();
            followersFilter.setMinFollowers(filterView.getFollowersGreaterThan());
            followersFilter.setMaxFollowers(filterView.getFollowersLessThan());
            filter.setFollowersFilter(followersFilter);
        }

    }

    private void addWordFilters(Filter filter) {
        if (CollectionUtils.isNotEmpty(filterView.getFilterWords())) {
            filter.setWordFilters(filterView.getFilterWords());
        }

    }

    private void addSerieFilters(Filter filter) {
        if (filterView.getThematic() != null) {
            SerieFilter serieFilter = new SerieFilter();
            serieFilter.setThematicId(filterView.getThematic().getId());
            if (filterView.getSerie() != null) {
                serieFilter.setSerieId(filterView.getSerie().getId());
            }
            if (filterView.getSubSerie() != null) {
                serieFilter.setSubSerieId(filterView.getSubSerie().getId());
            }
            filter.setSerieFilter(serieFilter);
        }

    }

    private void addCategoriesFilters(Filter filter) {
        if (CollectionUtils.isNotEmpty(filterView.getFilterCategories())) {
            List<CategoryFilter> categoryFilters = filterView
                    .getFilterCategories()
                    .stream()
                    .map(filterCategory -> {
                        CategoryFilter categoryFilter = new CategoryFilter();
                        categoryFilter.setCategoryGroup(filterCategory.getCategoryGroup().getId());
                        if (CollectionUtils.isNotEmpty(filterCategory.getCategories())) {
                            categoryFilter.setCategories(filterCategory.getCategories().stream().map(Category::getId)
                                    .toArray(size -> new Long[size]));
                        }
                        return categoryFilter;
                    }).collect(Collectors.toList());
            filter.setCategoryFilter(categoryFilters);
        }

    }

}
