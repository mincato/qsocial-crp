package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.SubjectCategorySetType;

public class SubjectCategorySetMapping implements Mapping<SubjectCategorySetType, SubjectCategorySet> {

    private static final String INDEX_NAME = "configuration";

    private static final String TYPE = "subjectCategorySet";

    private static SubjectCategorySetMapping instance;

    private SubjectCategorySetMapping() {
    }

    public static SubjectCategorySetMapping getInstance() {
        if (instance == null)
            instance = new SubjectCategorySetMapping();
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
        return SubjectCategorySetType.class;
    }

    @Override
    public SubjectCategorySetType getDocumentType(SubjectCategorySet document) {
        SubjectCategorySetType subjectCategorySetType = new SubjectCategorySetType();
        subjectCategorySetType.setId(document.getId());
        subjectCategorySetType.setDescription(document.getDescription());
        return subjectCategorySetType;
    }

    @Override
    public SubjectCategorySet getDocument(SubjectCategorySetType documentType) {
        SubjectCategorySet subjectCategorySet = new SubjectCategorySet();
        subjectCategorySet.setId(documentType.getId());
        subjectCategorySet.setDescription(documentType.getDescription());
        return subjectCategorySet;
    }

}
