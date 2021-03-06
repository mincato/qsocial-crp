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
        caseType.setIdCase(document.getId());
        caseType.setAdminUnits(document.getAdminUnits());
        caseType.setAsignedValue(document.getAsignedValue());
        caseType.setCaseCategoriesSet(document.getCaseCategoriesSet());
        caseType.setCaseCategories(document.getCaseCategories());
        caseType.setCloseDate(document.getCloseDate());
        caseType.setCoordinates(document.getCoordinates());
        caseType.setSubject(document.getSubject());
        caseType.setDescription(document.getDescription());
        caseType.setOpen(document.getOpen());
        caseType.setOpenDate(document.getOpenDate());
        caseType.setPendingResponse(document.getPendingResponse());
        caseType.setResolution(document.getResolution());
        caseType.setTitle(document.getTitle());
        caseType.setTriggerEvent(document.getTriggerEvent());
        caseType.setUnitValue(document.getUnitValue());
        caseType.setTriggerId(document.getTriggerId());
        caseType.setIdRootComment(document.getIdRootComment());
        caseType.setLastPostId(document.getLastPostId());
        caseType.setSegmentId(document.getSegmentId());
        caseType.setTeamId(document.getTeamId());
        caseType.setUserResolver(document.getUserResolver());
        caseType.setAssignee(document.getAssignee());
        caseType.setDomainId(document.getDomainId());
        caseType.setSource(document.getSource());
        caseType.setCaseSource(document.getCaseSource());
        caseType.setAssignee(document.getAssignee());
        caseType.setAttachments(document.getAttachments());
        caseType.setLastModifiedTimestamp(document.getLastModifiedTimestamp());
        caseType.setMessages(document.getMessages());
        caseType.setPriority(document.getPriority());
        caseType.setPriorityOrder(document.getPriority().getSortOrder());
        return caseType;
    }

    @Override
    public Case getDocument(CaseType documentType) {
        Case caseDocument = new Case();
        caseDocument.setId(documentType.getId());
        caseDocument.setAdminUnits(documentType.getAdminUnits());
        caseDocument.setAsignedValue(documentType.getAsignedValue());
        caseDocument.setCaseCategoriesSet(documentType.getCaseCategoriesSet());
        caseDocument.setCaseCategories(documentType.getCaseCategories());
        caseDocument.setCloseDate(documentType.getCloseDate());
        caseDocument.setCoordinates(documentType.getCoordinates());
        caseDocument.setSubject(documentType.getSubject());
        caseDocument.setDescription(documentType.getDescription());
        caseDocument.setOpen(documentType.getOpen());
        caseDocument.setOpenDate(documentType.getOpenDate());
        caseDocument.setPendingResponse(documentType.getPendingResponse());
        caseDocument.setResolution(documentType.getResolution());
        caseDocument.setTitle(documentType.getTitle());
        caseDocument.setTriggerEvent(documentType.getTriggerEvent());
        caseDocument.setUnitValue(documentType.getUnitValue());
        caseDocument.setTriggerId(documentType.getTriggerId());
        caseDocument.setIdRootComment(documentType.getIdRootComment());
        caseDocument.setLastPostId(documentType.getLastPostId());
        caseDocument.setSegmentId(documentType.getSegmentId());
        caseDocument.setTeamId(documentType.getTeamId());
        caseDocument.setAssignee(documentType.getAssignee());
        caseDocument.setDomainId(documentType.getDomainId());
        caseDocument.setUserResolver(documentType.getUserResolver());
        caseDocument.setSource(documentType.getSource());
        caseDocument.setCaseSource(documentType.getCaseSource());
        caseDocument.setAssignee(documentType.getAssignee());
        caseDocument.setAttachments(documentType.getAttachments());
        caseDocument.setLastModifiedTimestamp(documentType.getLastModifiedTimestamp());
        caseDocument.setMessages(documentType.getMessages());
        caseDocument.setPriority(documentType.getPriority());
        return caseDocument;
    }

}
