package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.SubjectCategoryType;

public class SubjectCategoryMapping implements Mapping<SubjectCategoryType, SubjectCategory> {

    private static final String INDEX_NAME = "configuration";

    private static final String TYPE = "subjectCategory";

    private static SubjectCategoryMapping instance;

    private SubjectCategoryMapping() {
    }

    public static SubjectCategoryMapping getInstance() {
        if (instance == null)
            instance = new SubjectCategoryMapping();
        return instance;
    }

    @Override
    public String getIndex() {
        return INDEX_NAME;
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
        return SubjectCategoryType.class;
    }

    @Override
    public SubjectCategoryType getDocumentType(SubjectCategory document) {
        SubjectCategoryType SubjectCategoryType = new SubjectCategoryType();
        SubjectCategoryType.setId(document.getId());
        SubjectCategoryType.setDescription(document.getDescription());
        return SubjectCategoryType;
    }

    @Override
    public SubjectCategory getDocument(SubjectCategoryType documentType) {
        SubjectCategory subjectCategory = new SubjectCategory();
        subjectCategory.setId(documentType.getId());
        subjectCategory.setDescription(documentType.getDescription());
        return subjectCategory;
    }
}
