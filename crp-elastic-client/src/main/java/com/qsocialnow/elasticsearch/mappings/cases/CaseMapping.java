package com.qsocialnow.elasticsearch.mappings.cases;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.elasticsearch.mappings.DynamicMapping;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.CaseType;

public class CaseMapping implements DynamicMapping, Mapping<CaseType, Case> {

    private String indexName;

    private static final String TYPE = "case";

    private static CaseMapping instance;

    private CaseMapping() {
    }

    public static CaseMapping getInstance() {
        if (instance == null)
            instance = new CaseMapping();
        return instance;
    }

    @Override
    public String getIndex() {
        return this.indexName;
    }

    @Override
    public void setIndex(String index) {
        this.indexName = index;
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
        return CaseType.class;
    }

    @Override
    public CaseType getDocumentType(Case document) {
        CaseType caseType = new CaseType();
        // caseType.setActionsRegistry(document.getActionsRegistry());
        caseType.setAdminUnits(document.getAdminUnits());
        caseType.setAsignedValue(document.getAsignedValue());
        caseType.setCaseCategories(document.getCaseCategories());
        caseType.setCloseDate(document.getCloseDate());
        caseType.setCoordinates(document.getCoordinates());
        caseType.setCustomer(document.getCustomer());
        caseType.setDescription(document.getDescription());
        caseType.setOpen(document.getOpen());
        caseType.setOpenDate(document.getOpenDate());
        caseType.setPendingResponse(document.getPendingResponse());
        caseType.setResolution(document.getResolution());
        caseType.setTitle(document.getTitle());
        caseType.setTriggerEvent(document.getTriggerEvent());
        caseType.setUnitValue(document.getUnitValue());
        return caseType;
    }

}
