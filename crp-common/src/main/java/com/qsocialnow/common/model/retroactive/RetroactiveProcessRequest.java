package com.qsocialnow.common.model.retroactive;

import java.util.List;

import com.qsocialnow.common.model.config.Category;
import com.qsocialnow.common.model.config.CategoryGroup;

public class RetroactiveProcessRequest {

    private String idRealTimeProduct;

    private Long tokenId;

    private String serieName;

    private Long serieId;

    private Long subSerieId;

    private String subSerieName;

    private Long[] subSeriesDefined;

    private Long timeFrom;

    private Long timeTo;

    private String[] languages;

    private Integer[] mediums;

    private Integer mediumType;

    private String[] connotations;

    private Long[] categories;

    private RangeRequest range;

    private AdministrativeUnitsFilter administrativeUnitsFilter;

    private WordsFilterRequestBean cloudsurfer;

    private String timezone;

    private String language;

    private Long createdDate;

    private String username;

    private List<Category> categoriesFilter;

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getSerieName() {
        return serieName;
    }

    public void setSerieName(String serieName) {
        this.serieName = serieName;
    }

    public Long getSerieId() {
        return serieId;
    }

    public void setSerieId(Long serieId) {
        this.serieId = serieId;
    }

    public Long getSubSerieId() {
        return subSerieId;
    }

    public void setSubSerieId(Long subSerieId) {
        this.subSerieId = subSerieId;
    }

    public Long[] getSubSeriesDefined() {
        return subSeriesDefined;
    }

    public void setSubSeriesDefined(Long[] subSeriesDefined) {
        this.subSeriesDefined = subSeriesDefined;
    }

    public Long getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Long timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Long getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Long timeTo) {
        this.timeTo = timeTo;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public Integer[] getMediums() {
        return mediums;
    }

    public void setMediums(Integer[] mediums) {
        this.mediums = mediums;
    }

    public Integer getMediumType() {
        return mediumType;
    }

    public void setMediumType(Integer mediumType) {
        this.mediumType = mediumType;
    }

    public String[] getConnotations() {
        return connotations;
    }

    public void setConnotations(String[] connotations) {
        this.connotations = connotations;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Long[] getCategories() {
        return categories;
    }

    public void setCategories(Long[] categories) {
        this.categories = categories;
    }

    public String getIdRealTimeProduct() {
        return idRealTimeProduct;
    }

    public void setIdRealTimeProduct(String idRealTimeProduct) {
        this.idRealTimeProduct = idRealTimeProduct;
    }

    public RangeRequest getRange() {
        return range;
    }

    public void setRange(RangeRequest range) {
        this.range = range;
    }

    public AdministrativeUnitsFilter getAdministrativeUnitsFilter() {
        return administrativeUnitsFilter;
    }

    public void setAdministrativeUnitsFilter(AdministrativeUnitsFilter administrativeUnitsFilter) {
        this.administrativeUnitsFilter = administrativeUnitsFilter;
    }

    public WordsFilterRequestBean getCloudsurfer() {
        return cloudsurfer;
    }

    public void setCloudsurfer(WordsFilterRequestBean cloudsurfer) {
        this.cloudsurfer = cloudsurfer;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getSubSerieName() {
        return subSerieName;
    }

    public void setSubSerieName(String subSerieName) {
        this.subSerieName = subSerieName;
    }

    public List<Category> getCategoriesFilter() {
        return categoriesFilter;
    }

    public void setCategoriesFilter(List<Category> categoriesFilter) {
        this.categoriesFilter = categoriesFilter;
    }

}
