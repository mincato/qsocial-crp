package com.qsocialnow.viewmodel.retroactive;

import java.util.List;

import com.qsocialnow.common.model.config.Category;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.retroactive.AdministrativeUnitsFilterBean;
import com.qsocialnow.common.model.retroactive.FollowersCountRange;
import com.qsocialnow.common.model.retroactive.WordsListFilterBean;
import com.qsocialnow.model.Connotation;
import com.qsocialnow.model.Language;

public class RetroactiveRequestView {

    private List<Media> medias;

    private List<Language> languages;

    private List<Connotation> connotations;

    private Long eventsFrom;

    private Long eventsTo;

    private FollowersCountRange followers;

    private WordsListFilterBean[] words;

    private AdministrativeUnitsFilterBean[] administrativeUnits;

    private String thematic;

    private String serie;

    private String subSerie;

    private List<Category> categories;

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public Long getEventsFrom() {
        return eventsFrom;
    }

    public void setEventsFrom(Long eventsFrom) {
        this.eventsFrom = eventsFrom;
    }

    public Long getEventsTo() {
        return eventsTo;
    }

    public void setEventsTo(Long eventsTo) {
        this.eventsTo = eventsTo;
    }

    public List<Connotation> getConnotations() {
        return connotations;
    }

    public void setConnotations(List<Connotation> connotations) {
        this.connotations = connotations;
    }

    public void setFollowers(FollowersCountRange followers) {
        this.followers = followers;
    }

    public FollowersCountRange getFollowers() {
        return followers;
    }

    public void setWords(WordsListFilterBean[] words) {
        this.words = words;
    }

    public WordsListFilterBean[] getWords() {
        return words;
    }

    public void setAdministrativeUnits(AdministrativeUnitsFilterBean[] administrativeUnits) {
        this.administrativeUnits = administrativeUnits;
    }

    public AdministrativeUnitsFilterBean[] getAdministrativeUnits() {
        return administrativeUnits;
    }

    public void setThematic(String thematic) {
        this.thematic = thematic;
    }

    public String getThematic() {
        return thematic;
    }

    public String getSubSerie() {
        return subSerie;
    }

    public void setSubSerie(String subSerie) {
        this.subSerie = subSerie;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
