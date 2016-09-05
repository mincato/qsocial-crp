package com.qsocialnow.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.qsocialnow.common.model.config.AdmUnitFilter;
import com.qsocialnow.common.model.config.WordFilter;

public class FilterView {

    private Connotation connotation;

    private Date startDateTime;

    private Date endDateTime;

    private Long followersGreaterThan;

    private Long followersLessThan;

    private List<WordFilter> filterWords;

    private List<AdmUnitFilter> admUnitFilters;

    public FilterView() {
        filterWords = new ArrayList<>();
    }

    public Connotation getConnotation() {
        return connotation;
    }

    public void setConnotation(Connotation connotation) {
        this.connotation = connotation;
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

}
