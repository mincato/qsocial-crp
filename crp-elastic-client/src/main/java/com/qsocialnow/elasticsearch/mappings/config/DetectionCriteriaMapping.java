package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.DetectionCriteriaType;

public class DetectionCriteriaMapping implements Mapping<DetectionCriteriaType, DetectionCriteria> {

    private static final String INDEX_NAME = "configuration";

    private static final String TYPE = "detectioncriteria";

    private static DetectionCriteriaMapping instance;

    private DetectionCriteriaMapping() {
    }

    public static DetectionCriteriaMapping getInstance() {
        if (instance == null)
            instance = new DetectionCriteriaMapping();
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
        return DetectionCriteria.class;
    }

    @Override
    public DetectionCriteriaType getDocumentType(DetectionCriteria document) {
        DetectionCriteriaType criteria = new DetectionCriteriaType();
        criteria.setAccionCriterias(document.getActionCriterias());
        criteria.setFilter(document.getFilter());
        criteria.setSequenceOrder(document.getSequenceOrder());
        criteria.setValidateFrom(document.getValidateFrom());
        criteria.setValidateTo(document.getValidateTo());

        return criteria;
    }

    @Override
    public DetectionCriteria getDocument(DetectionCriteriaType documentType) {
        DetectionCriteria criteria = new DetectionCriteria();
        criteria.setId(documentType.getId());
        criteria.setAccionCriterias(documentType.getActionCriterias());
        criteria.setFilter(documentType.getFilter());
        criteria.setSequenceOrder(documentType.getSequenceOrder());
        criteria.setValidateFrom(documentType.getValidateFrom());
        criteria.setValidateTo(documentType.getValidateTo());

        return criteria;
    }
}
