package com.qsocialnow.elasticsearch.mappings.cases;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.elasticsearch.mappings.DynamicMapping;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.SubjectType;

public class SubjectMapping implements DynamicMapping, Mapping<SubjectType, Subject> {

    private static final String TYPE = "subject";

    private String indexName;

    private static SubjectMapping instance;

    private SubjectMapping() {
    }

    public static SubjectMapping getInstance() {
        if (instance == null)
            instance = new SubjectMapping();
        return instance;
    }

    @Override
    public void setIndex(String index) {
        this.indexName = index;
    }

    @Override
    public String getIndex() {
        return indexName;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getMappingDefinition() {
        JSONObject mapping = new JSONObject();
        return mapping.toJSONString();
    }

    @Override
    public Class<?> getClassType() {
        return SubjectType.class;
    }

    @Override
    public SubjectType getDocumentType(Subject document) {
        SubjectType subjectType = new SubjectType();
        subjectType.setId(document.getId());
        subjectType.setLastAccionDate(document.getLastAccionDate());
        subjectType.setSignedDate(document.getSignedDate());
        subjectType.setSourceId(document.getSourceId());
        subjectType.setIdentifier(document.getIdentifier());
        subjectType.setSource(document.getSource());
        subjectType.setProfileImage(document.getProfileImage());
        subjectType.setSubjectCategory(document.getSubjectCategory());
        subjectType.setSubjectCategorySet(document.getSubjectCategorySet());
        subjectType.setLocation(document.getLocation());
        subjectType.setLocationMethod(document.getLocationMethod());
        subjectType.setSourceName(document.getSourceName());
        return subjectType;
    }

    @Override
    public Subject getDocument(SubjectType documentType) {
        Subject subject = new Subject();
        subject.setId(documentType.getId());

        subject.setLastAccionDate(documentType.getLastAccionDate());

        subject.setSignedDate(documentType.getSignedDate());
        subject.setSourceId(documentType.getSourceId());
        subject.setIdentifier(documentType.getIdentifier());
        subject.setSource(documentType.getSource());
        subject.setProfileImage(documentType.getProfileImage());
        subject.setSubjectCategory(documentType.getSubjectCategory());
        subject.setSubjectCategorySet(documentType.getSubjectCategorySet());

        subject.setLocation(documentType.getLocation());
        subject.setLocationMethod(documentType.getLocationMethod());
        subject.setSourceName(documentType.getSourceName());
        return subject;
    }
}
