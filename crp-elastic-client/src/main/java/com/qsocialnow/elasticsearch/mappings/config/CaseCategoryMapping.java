package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.CaseCategoryType;
import com.qsocialnow.elasticsearch.mappings.types.config.TeamType;

public class CaseCategoryMapping implements Mapping<CaseCategoryType, CaseCategory> {

    private static final String INDEX_NAME = "configuration";

    private static final String TYPE = "caseCategory";

    private static CaseCategoryMapping instance;

    private CaseCategoryMapping() {
    }

    public static CaseCategoryMapping getInstance() {
        if (instance == null)
            instance = new CaseCategoryMapping();
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
        return CaseCategoryType.class;
    }

    @Override
    public CaseCategoryType getDocumentType(CaseCategory document) {
        CaseCategoryType caseCategoryType = new CaseCategoryType();
        caseCategoryType.setId(document.getId());
        caseCategoryType.setDescription(document.getDescription());
        return caseCategoryType;
    }

    @Override
    public CaseCategory getDocument(CaseCategoryType documentType) {
        CaseCategory caseCategory = new CaseCategory();
        caseCategory.setId(documentType.getId());
        caseCategory.setDescription(documentType.getDescription());
        return caseCategory;
    }

}
