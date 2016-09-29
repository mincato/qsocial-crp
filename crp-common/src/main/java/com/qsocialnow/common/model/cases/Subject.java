package com.qsocialnow.common.model.cases;

import java.io.Serializable;
import java.util.Date;

public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    // identifier aka usuarioOriginal
    private String identifier;

    // sourceId aka idUsuarioOriginal
    private String sourceId;

    // source aka medioId
    private String source;

    private String profileImage;

    private String name;

    private String lastName;

    private String address;

    private Date signedDate;

    private Date lastAccionDate;

    private Integer age;

    private String subjectCategorySet;

    private String subjectCategory;

    private ContactInfo contactInfo;

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
        if (identifier != null) {
            String[] identifierTokens = identifier.split("\\s+");
            if (identifierTokens.length > 0)
                this.identifier = identifierTokens[0];
            else
                this.identifier = identifier;
        }
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSubjectCategorySet() {
        return subjectCategorySet;
    }

    public void setSubjectCategorySet(String subjectCategorySet) {
        this.subjectCategorySet = subjectCategorySet;
    }

    public String getSubjectCategory() {
        return subjectCategory;
    }

    public void setSubjectCategory(String subjectCategory) {
        this.subjectCategory = subjectCategory;
    }
}
