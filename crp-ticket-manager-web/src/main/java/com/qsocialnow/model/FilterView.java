package com.qsocialnow.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.qsocialnow.common.model.config.AdmUnitFilter;
import com.qsocialnow.common.model.config.WordFilter;

public class FilterView {

    private Date startDateTime;

    private Date endDateTime;

    private Long followersGreaterThan;

    private Long followersLessThan;

    private List<WordFilter> filterWords;

    private List<AdmUnitFilter> admUnitFilters;

    private Thematic thematic;

    private Series serie;

    private SubSeries subSerie;

    private List<CategoryFilterView> filterCategories;

    public FilterView() {
        filterWords = new ArrayList<>();
        filterCategories = new ArrayList<>();
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
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

    public List<WordFilter> getFilterWords() {
        return filterWords;
    }

    public void setFilterWords(List<WordFilter> filterWords) {
        this.filterWords = filterWords;
    }

    public void setAdmUnitFilters(List<AdmUnitFilter> admUnitFilters) {
        this.admUnitFilters = admUnitFilters;
    }

    public List<AdmUnitFilter> getAdmUnitFilters() {
        return admUnitFilters;
    }

    public Thematic getThematic() {
        return thematic;
    }

    public void setThematic(Thematic thematic) {
        this.thematic = thematic;
    }

    public Series getSerie() {
        return serie;
    }

    public void setSerie(Series serie) {
        this.serie = serie;
    }

    public SubSeries getSubSerie() {
        return subSerie;
    }

    public void setSubSerie(SubSeries subSerie) {
        this.subSerie = subSerie;
    }

    public List<CategoryFilterView> getFilterCategories() {
        return filterCategories;
    }

    public void setFilterCategories(List<CategoryFilterView> filterCategories) {
        this.filterCategories = filterCategories;
    }

}
