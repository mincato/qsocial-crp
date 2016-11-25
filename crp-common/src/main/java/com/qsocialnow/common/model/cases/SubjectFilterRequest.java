package com.qsocialnow.common.model.cases;

import java.util.Date;

import com.qsocialnow.common.model.pagination.PageRequest;

public class SubjectFilterRequest {

    private boolean filterActive;

    private PageRequest pageRequest;

    private String id;

    private String keyword;

    // identifier aka usuarioOriginal
    private String identifier;

    // sourceId aka idUsuarioOriginal
    private String sourceId;

    // source aka medioId
    private Long source;

    private String profileImage;

    private Date signedDate;

    private Date lastAccionDate;

    private String sourceName;

    private String location;

    private String personId;

    public boolean isFilterActive() {
        return filterActive;
    }

    public void setFilterActive(boolean filterActive) {
        this.filterActive = filterActive;
    }

    public PageRequest getPageRequest() {
        return pageRequest;
    }

    public void setPageRequest(PageRequest pageRequest) {
        this.pageRequest = pageRequest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Date getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(Date signedDate) {
        this.signedDate = signedDate;
    }

    public Date getLastAccionDate() {
        return lastAccionDate;
    }

    public void setLastAccionDate(Date lastAccionDate) {
        this.lastAccionDate = lastAccionDate;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
