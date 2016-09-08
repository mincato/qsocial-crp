package com.qsocialnow.common.model.config;

import java.util.List;

public class Filter {

    private String id;

    private MediaFilter mediaFilter;

    private LanguageFilter languageFilter;

    private PeriodFilter periodFilter;

    private ConnotationFilter connotationFilter;

    private FollowersFilter followersFilter;

    private List<WordFilter> wordFilters;

    private List<AdmUnitFilter> admUnitFilter;

    private List<CategoryFilter> categoryFilter;

    private SerieFilter serieFilter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MediaFilter getMediaFilter() {
        return mediaFilter;
    }

    public void setMediaFilter(MediaFilter mediaFilter) {
        this.mediaFilter = mediaFilter;
    }

    public LanguageFilter getLanguageFilter() {
        return languageFilter;
    }

    public void setLanguageFilter(LanguageFilter languageFilter) {
        this.languageFilter = languageFilter;
    }

    public PeriodFilter getPeriodFilter() {
        return periodFilter;
    }

    public void setPeriodFilter(PeriodFilter periodFilter) {
        this.periodFilter = periodFilter;
    }

    public ConnotationFilter getConnotationFilter() {
        return connotationFilter;
    }

    public void setConnotationFilter(ConnotationFilter connotationFilter) {
        this.connotationFilter = connotationFilter;
    }

    public FollowersFilter getFollowersFilter() {
        return followersFilter;
    }

    public void setFollowersFilter(FollowersFilter followersFilter) {
        this.followersFilter = followersFilter;
    }

    public List<WordFilter> getWordFilters() {
        return wordFilters;
    }

    public void setWordFilters(List<WordFilter> wordFilters) {
        this.wordFilters = wordFilters;
    }

    public List<AdmUnitFilter> getAdmUnitFilter() {
        return admUnitFilter;
    }

    public void setAdmUnitFilter(List<AdmUnitFilter> admUnitFilter) {
        this.admUnitFilter = admUnitFilter;
    }

    public List<CategoryFilter> getCategoryFilter() {
        return categoryFilter;
    }

    public void setCategoryFilter(List<CategoryFilter> categoryFilter) {
        this.categoryFilter = categoryFilter;
    }

    public SerieFilter getSerieFilter() {
        return serieFilter;
    }

    public void setSerieFilter(SerieFilter serieFilter) {
        this.serieFilter = serieFilter;
    }

}
