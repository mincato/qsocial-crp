package com.qsocialnow.common.model.cases;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.qsocialnow.common.model.event.GeoSocialEventLocationMethod;
import com.qsocialnow.common.util.SubjectIdentifierNormalizer;

public class Subject implements Serializable {

    private static final long serialVersionUID = -5000381409758581245L;

    private String id;

    // identifier aka usuarioOriginal
    private String identifier;

    // sourceId aka idUsuarioOriginal
    private String sourceId;

    // source aka medioId
    private Long source;

    private String profileImage;

    private Date signedDate;

    private Date lastAccionDate;

    private Set<String> subjectCategorySet;

    private Set<String> subjectCategory;

    private GeoSocialEventLocationMethod locationMethod;

    private String sourceName;

    private String location;

    private String personId;

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
        this.identifier = SubjectIdentifierNormalizer.normalize(identifier);
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

    public Set<String> getSubjectCategorySet() {
        return subjectCategorySet;
    }

    public void setSubjectCategorySet(Set<String> subjectCategorySet) {
        this.subjectCategorySet = subjectCategorySet;
    }

    public Set<String> getSubjectCategory() {
        return subjectCategory;
    }

    public void setSubjectCategory(Set<String> subjectCategory) {
        this.subjectCategory = subjectCategory;
    }

    public void addSubjectCategoriesSet(List<String> subjectCategoriesSet) {
        if (this.subjectCategorySet == null) {
            this.subjectCategorySet = new HashSet<>();
        }
        this.subjectCategorySet.addAll(subjectCategoriesSet);
    }

    public void addSubjectCategories(List<String> subjectCategories) {
        if (this.subjectCategory == null) {
            this.subjectCategory = new HashSet<>();
        }
        this.subjectCategory.addAll(subjectCategories);

    }

    public GeoSocialEventLocationMethod getLocationMethod() {
        return locationMethod;
    }

    public void setLocationMethod(GeoSocialEventLocationMethod locationMethod) {
        this.locationMethod = locationMethod;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

}
