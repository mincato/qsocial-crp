package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.elasticsearch.mappings.ChildMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.TriggerType;

public class TriggerMapping implements ChildMapping<TriggerType, Trigger> {

    private static final String INDEX_NAME = "configuration";

    private static final String TYPE = "trigger";

    private static TriggerMapping instance;

    private String idParent;

    private TriggerMapping() {
    }

    public static TriggerMapping getInstance() {
        if (instance == null)
            instance = new TriggerMapping();
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
        triggerType.setSegments(document.getSegments());
        triggerType.setStatus(document.getStatus());
        return triggerType;
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
    public Trigger getDocument(TriggerType documentType) {
        Trigger trigger = new Trigger();
        trigger.setId(documentType.getId());
        trigger.setCustomerGroups(documentType.getCustomerGroups());
        trigger.setDescription(documentType.getDescription());
        trigger.setEnd(documentType.getEnd());
        trigger.setInit(documentType.getInit());
        trigger.setName(documentType.getName());
        trigger.setResolutions(documentType.getResolutions());
        trigger.setSegments(documentType.getSegments());
        trigger.setStatus(documentType.getStatus());
        return trigger;
    }

}
