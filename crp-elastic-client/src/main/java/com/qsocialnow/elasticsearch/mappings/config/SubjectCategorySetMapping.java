package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.SubjectCategorySetType;

public class SubjectCategorySetMapping implements Mapping<SubjectCategorySetType, SubjectCategorySet> {

    private static final String TYPE = "subjectCategorySet";

    private static SubjectCategorySetMapping instance;

    private final String index;

    private SubjectCategorySetMapping(String index) {
        this.index = index;
    }

    public static SubjectCategorySetMapping getInstance(String index) {
        if (instance == null)
            instance = new SubjectCategorySetMapping(index);
        return instance;
    }

    @Override
    public String getIndex() {
        return index;
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
        subjectCategorySetType.setActive(document.isActive());
        return subjectCategorySetType;
    }

    @Override
    public SubjectCategorySet getDocument(SubjectCategorySetType documentType) {
        SubjectCategorySet subjectCategorySet = new SubjectCategorySet();
        subjectCategorySet.setId(documentType.getId());
        subjectCategorySet.setDescription(documentType.getDescription());
        subjectCategorySet.setActive(documentType.isActive());
        return subjectCategorySet;
    }

}
