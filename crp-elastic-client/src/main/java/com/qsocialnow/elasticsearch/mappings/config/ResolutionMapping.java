package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.elasticsearch.mappings.ChildMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.ResolutionType;

public class ResolutionMapping implements ChildMapping<ResolutionType, Resolution> {

    private static final String INDEX_NAME = "configuration";

    private static final String TYPE = "resolution";

    private static ResolutionMapping instance;

    private String idParent;

    private ResolutionMapping() {
    }

    public static ResolutionMapping getInstance() {
        if (instance == null)
            instance = new ResolutionMapping();
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
        return ResolutionType.class;
    }

    @Override
    public ResolutionType getDocumentType(Resolution document) {
        ResolutionType resolutionType = new ResolutionType();
        resolutionType.setDescription(document.getDescription());
        return resolutionType;
    }

    @Override
    public String getIdParent() {
        return idParent;
    }

    @Override
    public void setIdParent(String idParent) {
        this.idParent = idParent;
    }

    @Override
    public String getParentType() {
        return DomainMapping.TYPE;
    }

    @Override
    public Resolution getDocument(ResolutionType documentType) {
        Resolution resolution = new Resolution();
        resolution.setId(documentType.getId());
        resolution.setDescription(documentType.getDescription());
        return resolution;
    }

}