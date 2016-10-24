package com.qsocialnow.elasticsearch.mappings.cases;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.elasticsearch.mappings.DynamicMapping;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.ActionRegistryType;

public class ActionRegistryMapping implements DynamicMapping, Mapping<ActionRegistryType, ActionRegistry> {

    private String indexName;

    private static final String TYPE = "actionregistry";

    private static ActionRegistryMapping instance;

    private ActionRegistryMapping() {
    }

    public static ActionRegistryMapping getInstance() {
        if (instance == null)
            instance = new ActionRegistryMapping();
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
        return ActionRegistryType.class;
    }

    @Override
    public ActionRegistryType getDocumentType(ActionRegistry document) {
        ActionRegistryType actionRegistryType = new ActionRegistryType();
        actionRegistryType.setAction(document.getAction());
        actionRegistryType.setAutomatic(document.getAutomatic());
        actionRegistryType.setComment(document.getComment());
        actionRegistryType.setDate(document.getDate());
        actionRegistryType.setEvent(document.getEvent());
        actionRegistryType.setType(document.getType());
        actionRegistryType.setUserName(document.getUserName());
        actionRegistryType.setDeepLink(document.getDeepLink());
        return actionRegistryType;
    }

    @Override
    public ActionRegistry getDocument(ActionRegistryType documentType) {
        ActionRegistry actionRegistry = new ActionRegistry();
        actionRegistry.setId(documentType.getId());
        actionRegistry.setAction(documentType.getAction());
        actionRegistry.setAutomatic(documentType.getAutomatic());
        actionRegistry.setComment(documentType.getComment());
        actionRegistry.setDate(documentType.getDate());
        actionRegistry.setEvent(documentType.getEvent());
        actionRegistry.setType(documentType.getType());
        actionRegistry.setUserName(documentType.getUserName());
        actionRegistry.setDeepLink(documentType.getDeepLink());
        return actionRegistry;
    }

}
