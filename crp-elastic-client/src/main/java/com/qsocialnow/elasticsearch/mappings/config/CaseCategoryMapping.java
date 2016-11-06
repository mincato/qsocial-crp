package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.CaseCategoryType;

public class CaseCategoryMapping implements Mapping<CaseCategoryType, CaseCategory> {

    public static final String TYPE = "caseCategory";

    private static CaseCategoryMapping instance;

    private final String index;

    private CaseCategoryMapping(String index) {
        this.index = index;
    }

    public static CaseCategoryMapping getInstance(String index) {
        if (instance == null)
            instance = new CaseCategoryMapping(index);
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
        return CaseCategoryType.class;
    }

    @Override
    public CaseCategoryType getDocumentType(CaseCategory document) {
        CaseCategoryType caseCategoryType = new CaseCategoryType();
        caseCategoryType.setId(document.getId());
        caseCategoryType.setDescription(document.getDescription());
        caseCategoryType.setActive(document.getActive());
        return caseCategoryType;
    }

    @Override
    public CaseCategory getDocument(CaseCategoryType documentType) {
        CaseCategory caseCategory = new CaseCategory();
        caseCategory.setId(documentType.getId());
        caseCategory.setDescription(documentType.getDescription());
        caseCategory.setActive(documentType.getActive());
        return caseCategory;
    }

}
