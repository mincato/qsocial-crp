package com.qsocialnow.common.model.cases;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.qsocialnow.common.model.config.Media;

public class SubjectListView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String lastName;

    private String identifier;

    private String address;

    private Date signedDate;

    private Date lastAccion;

    private Integer age;

    private String sourceId;

    private Long source;

    private Set<String> subjectCategorySet;

    private Set<String> subjectCategory;

    private ContactInfo contactInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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

    public Date getLastAccion() {
        return lastAccion;
    }

    public void setLastAccion(Date lastAccion) {
        this.lastAccion = lastAccion;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Media getMedia() {
        return Media.getByValue(this.source);
    }
}
