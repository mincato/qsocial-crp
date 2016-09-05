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
import org.apache.commons.lang3.StringUtils;
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
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.ConnotationFilter;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.FollowersFilter;
import com.qsocialnow.common.model.config.LanguageFilter;
import com.qsocialnow.common.model.config.MediaFilter;
import com.qsocialnow.common.model.config.PeriodFilter;
import com.qsocialnow.common.model.config.WordFilter;
import com.qsocialnow.common.model.config.WordFilterType;
import com.qsocialnow.model.Connotation;
import com.qsocialnow.model.ConnotationView;
import com.qsocialnow.model.FilterView;
import com.qsocialnow.model.Language;
import com.qsocialnow.model.LanguageView;
import com.qsocialnow.model.Media;
import com.qsocialnow.model.MediaView;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class CreateCriteriaViewModel implements Serializable {

    private static final long serialVersionUID = -4119198423406156946L;

    private DetectionCriteria currentCriteria;

    private List<MediaView> mediaTypes;

    private List<ConnotationView> connotations;

    private List<LanguageView> languages;

    private FilterView filterView;

    private boolean saved;

    private Set<WordFilterType> wordFilterTypeOptions;

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
        filterView = new FilterView();
        wordFilterTypeOptions = Arrays.stream(WordFilterType.values()).collect(Collectors.toSet());
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
        if (StringUtils.isNotEmpty(filterView.getFollowersGreaterThan())
                || StringUtils.isNotEmpty(filterView.getFollowersLessThan())) {
            FollowersFilter followersFilter = new FollowersFilter();
            followersFilter.setMinFollowers(Long.parseLong(filterView.getFollowersGreaterThan()));
            followersFilter.setMaxFollowers(Long.parseLong(filterView.getFollowersLessThan()));
            filter.setFollowersFilter(followersFilter);
        }

    }

    private void addWordFilters(Filter filter) {
        if (CollectionUtils.isNotEmpty(filterView.getFilterWords())) {
            filter.setWordFilters(filterView.getFilterWords());
        }

    }
}
