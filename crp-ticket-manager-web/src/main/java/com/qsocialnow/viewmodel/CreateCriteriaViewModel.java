package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.ListModelList;

import com.qsocialnow.common.model.config.CategoryFilter;
import com.qsocialnow.common.model.config.ConnotationFilter;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.FollowersFilter;
import com.qsocialnow.common.model.config.LanguageFilter;
import com.qsocialnow.common.model.config.MediaFilter;
import com.qsocialnow.common.model.config.PeriodFilter;
import com.qsocialnow.common.model.config.SerieFilter;
import com.qsocialnow.common.model.config.WordFilter;
import com.qsocialnow.common.model.config.WordFilterType;
import com.qsocialnow.model.Category;
import com.qsocialnow.model.CategoryFilterView;
import com.qsocialnow.model.CategoryGroup;
import com.qsocialnow.model.CategoryGroupBySerieIdInput;
import com.qsocialnow.model.Connotation;
import com.qsocialnow.model.ConnotationView;
import com.qsocialnow.model.FilterView;
import com.qsocialnow.model.Language;
import com.qsocialnow.model.LanguageView;
import com.qsocialnow.model.Media;
import com.qsocialnow.model.MediaView;
import com.qsocialnow.model.Series;
import com.qsocialnow.model.SubSeries;
import com.qsocialnow.model.Thematic;
import com.qsocialnow.services.CategoryService;
import com.qsocialnow.services.ThematicService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class CreateCriteriaViewModel implements Serializable {

    private static final long serialVersionUID = -4119198423406156946L;

    @WireVariable("mockThematicService")
    private ThematicService thematicService;

    @WireVariable("mockCategoryService")
    private CategoryService categoryService;

    private DetectionCriteria currentCriteria;

    private List<MediaView> mediaTypes;

    private List<ConnotationView> connotations;

    private List<LanguageView> languages;

    private FilterView filterView;

    private boolean saved;

    private Set<WordFilterType> wordFilterTypeOptions;

    private ListModelList<Thematic> thematicsOptions = new ListModelList<>();

    private ListModelList<Series> serieOptions = new ListModelList<>();

    private ListModelList<SubSeries> subSerieOptions = new ListModelList<>();

    private List<CategoryGroup> categoryGroupOptions = new ArrayList<>();

    private CategoryFilterView filterCategory;

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

    public List<CategoryGroup> getCategoryGroupOptions() {
        if (filterView.getSerie() != null && categoryGroupOptions.isEmpty()) {
            CategoryGroupBySerieIdInput categoryGroupBySerieIdInput = new CategoryGroupBySerieIdInput();
            categoryGroupBySerieIdInput.setId(filterView.getSerie().getId());
            categoryGroupBySerieIdInput.setThematicId(filterView.getThematic().getId());
            categoryGroupOptions.addAll(categoryService.findBySerieId(categoryGroupBySerieIdInput));
        }
        return categoryGroupOptions;
    }

    public ListModelList<Series> getSerieOptions() {
        if (filterView.getThematic() != null && serieOptions.isEmpty()) {
            serieOptions.addAll(thematicsOptions.stream()
                    .filter(thematic -> filterView.getThematic().getId().equals(thematic.getId())).findFirst().get()
                    .getSeries());
        }
        return serieOptions;
    }

    public List<SubSeries> getSubSerieOptions() {
        if (filterView.getSerie() != null && subSerieOptions.isEmpty()) {
            subSerieOptions.addAll(serieOptions.stream()
                    .filter(serie -> filterView.getSerie().getId().equals(serie.getId())).findFirst().get()
                    .getSubSeries());
        }
        return subSerieOptions;
    }

    public CategoryFilterView getFilterCategory() {
        return filterCategory;
    }

    public boolean isSaved() {
        return saved;
    }

    @Command
    @NotifyChange("saved")
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
        saved = true;
    }

    @Command
    public void close(@ContextParam(ContextType.VIEW) Component comp) {
        comp.detach();
        if (saved) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("detectionCriteria", currentCriteria);
            BindUtils.postGlobalCommand(null, null, "addCriteria", args);
        }
    }

    @Init
    public void init() {
        currentCriteria = new DetectionCriteria();
        filterCategory = null;
        filterView = new FilterView();
        wordFilterTypeOptions = Arrays.stream(WordFilterType.values()).collect(Collectors.toSet());
        thematicsOptions.addAll(thematicService.findAll());
        initMedias();
        initConnotations();
        initLanguages();

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
    @NotifyChange("filter")
    public void addFilterWord() {
        this.filterView.getFilterWords().add(new WordFilter());
    }

    @Command
    @NotifyChange("filter")
    public void removeFilterWord(@BindingParam("filter") WordFilter filter) {
        this.filterView.getFilterWords().remove(filter);
    }

    @Command
    @NotifyChange("filter")
    public void addFilterCategory() {
        this.filterView.getFilterCategories().add(new CategoryFilterView(new ArrayList<>(categoryGroupOptions)));
    }

    @Command
    @NotifyChange("filter")
    public void removeFilterCategory(@BindingParam("filter") CategoryFilterView filter) {
        this.filterView.getFilterCategories().remove(filter);
    }

    @Command
    @NotifyChange("filter")
    public void removeCategory(@BindingParam("filter") CategoryFilterView filter,
            @BindingParam("category") Category category) {
        filter.getCategories().remove(category);
    }

    @Command
    @NotifyChange({ "serieOptions", "filter", "categoryGroupOptions", "subSerieOptions" })
    public void selectThematic() {
        serieOptions.clear();
        filterView.setSerie(null);
        selectSerie();
    }

    @Command
    @NotifyChange({ "subSerieOptions", "categoryGroupOptions", "filter" })
    public void selectSerie() {
        subSerieOptions.clear();
        filterView.setSubSerie(null);
        categoryGroupOptions.clear();
    }

    @Command
    @NotifyChange({ "filter", "filterCategory" })
    public void selectGroupCategory(@BindingParam("filter") CategoryFilterView filterCategory) {
        filterCategory.getCategoryOptions().clear();
        filterCategory.setCategories(null);
        this.filterCategory = filterCategory;
    }

    @Command
    @NotifyChange({ "filterCategory", "filter" })
    public void closeCategories() {
        this.filterCategory = null;
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
