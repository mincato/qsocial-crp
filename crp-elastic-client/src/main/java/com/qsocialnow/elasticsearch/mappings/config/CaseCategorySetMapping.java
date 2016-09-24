package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.CaseCategorySetType;

public class CaseCategorySetMapping implements Mapping<CaseCategorySetType, CaseCategorySet> {

    private static final String INDEX_NAME = "configuration";

    private static final String TYPE = "caseCategorySet";

    private static CaseCategorySetMapping instance;

    private CaseCategorySetMapping() {
    }

    public static CaseCategorySetMapping getInstance() {
        if (instance == null)
            instance = new CaseCategorySetMapping();
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
        return CaseCategorySetType.class;
    }

    @Override
    public CaseCategorySetType getDocumentType(CaseCategorySet document) {
        CaseCategorySetType caseCategorySetType = new CaseCategorySetType();
        caseCategorySetType.setId(document.getId());
        caseCategorySetType.setDescription(document.getDescription());
        return caseCategorySetType;
    }

    @Override
    public CaseCategorySet getDocument(CaseCategorySetType documentType) {
        CaseCategorySet caseCategorySet = new CaseCategorySet();
        caseCategorySet.setId(documentType.getId());
        caseCategorySet.setDescription(documentType.getDescription());
        return caseCategorySet;
    }

}
