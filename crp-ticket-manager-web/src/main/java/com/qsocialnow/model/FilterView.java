package com.qsocialnow.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.qsocialnow.common.model.config.WordFilter;

public class FilterView {

    private Connotation connotation;

    private Date startDateTime;

    private Date endDateTime;

    private String followersGreaterThan;

    private String followersLessThan;

    private List<WordFilter> filterWords;

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

    public String getFollowersGreaterThan() {
        return followersGreaterThan;
    }

    public void setFollowersGreaterThan(String followersGreaterThan) {
        this.followersGreaterThan = followersGreaterThan;
    }

    public String getFollowersLessThan() {
        return followersLessThan;
    }

    public void setFollowersLessThan(String followersLessThan) {
        this.followersLessThan = followersLessThan;
    }

    public List<WordFilter> getFilterWords() {
        return filterWords;
    }

    public void setFilterWords(List<WordFilter> filterWords) {
        this.filterWords = filterWords;
    }

}
