package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.CaseCategorySetType;

public class CaseCategorySetMapping implements Mapping<CaseCategorySetType, CaseCategorySet> {

    private static final String TYPE = "caseCategorySet";

    private static CaseCategorySetMapping instance;

    private final String index;

    private CaseCategorySetMapping(String index) {
        this.index = index;
    }

    public static CaseCategorySetMapping getInstance(String index) {
        if (instance == null)
            instance = new CaseCategorySetMapping(index);
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
        return CaseCategorySetType.class;
    }

    @Override
    public CaseCategorySetType getDocumentType(CaseCategorySet document) {
        CaseCategorySetType caseCategorySetType = new CaseCategorySetType();
        caseCategorySetType.setId(document.getId());
        caseCategorySetType.setDescription(document.getDescription());
        caseCategorySetType.setActive(document.getActive());
        return caseCategorySetType;
    }

    @Override
    public CaseCategorySet getDocument(CaseCategorySetType documentType) {
        CaseCategorySet caseCategorySet = new CaseCategorySet();
        caseCategorySet.setId(documentType.getId());
        caseCategorySet.setDescription(documentType.getDescription());
        caseCategorySet.setActive(documentType.getActive());
        return caseCategorySet;
    }

}
