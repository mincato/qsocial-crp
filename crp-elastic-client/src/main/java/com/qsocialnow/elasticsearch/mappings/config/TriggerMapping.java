package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.TriggerType;

public class TriggerMapping implements Mapping<TriggerType, Trigger> {

    public static final String TYPE = "trigger";

    private static TriggerMapping instance;

    private final String index;

    private TriggerMapping(String index) {
        this.index = index;
    }

    public static TriggerMapping getInstance(String index) {
        if (instance == null)
            instance = new TriggerMapping(index);
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
        return TriggerType.class;
    }

    @Override
    public TriggerType getDocumentType(Trigger document) {
        TriggerType triggerType = new TriggerType();
        triggerType.setCustomerGroups(document.getCustomerGroups());
        triggerType.setDescription(document.getDescription());
        triggerType.setEnd(document.getEnd());
        triggerType.setInit(document.getInit());
        triggerType.setName(document.getName());
        triggerType.setResolutions(document.getResolutions());
        triggerType.setStatus(document.getStatus());
        triggerType.setCaseCategoriesSetIds(document.getCaseCategoriesSetIds());
        triggerType.setSubjectCategoriesSetIds(document.getSubjectCategoriesSetIds());
        return triggerType;
    }

    @Override
    public Trigger getDocument(TriggerType documentType) {
        Trigger trigger = new Trigger();
        trigger.setId(documentType.getId());
        trigger.setCustomerGroups(documentType.getCustomerGroups());
        trigger.setDescription(documentType.getDescription());
        trigger.setEnd(documentType.getEnd());
        trigger.setInit(documentType.getInit());
        trigger.setName(documentType.getName());
        trigger.setResolutions(documentType.getResolutions());
        trigger.setStatus(documentType.getStatus());
        trigger.setCaseCategoriesSetIds(documentType.getCaseCategoriesSetIds());
        trigger.setSubjectCategoriesSetIds(documentType.getSubjectCategoriesSetIds());
        trigger.setDomainId(documentType.getDomainId());
        return trigger;
    }

}
