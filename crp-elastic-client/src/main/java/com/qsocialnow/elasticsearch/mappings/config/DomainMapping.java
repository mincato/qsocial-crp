package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.DomainType;

public class DomainMapping implements Mapping<DomainType, Domain> {

    private static final String INDEX_NAME = "configuration";

    public static final String TYPE = "domain";

    private static DomainMapping instance;

    private DomainMapping() {
    }

    public static DomainMapping getInstance() {
        if (instance == null)
            instance = new DomainMapping();
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
    @SuppressWarnings("unchecked")
    public String getMappingDefinition() {
        JSONObject mapping = new JSONObject();

        JSONObject domain = new JSONObject();
        JSONObject properties = new JSONObject();

        JSONObject id = new JSONObject();
        id.put("type", "string");
        id.put("store", "true");

        JSONObject name = new JSONObject();
        name.put("type", "string");
        name.put("store", "true");

        JSONArray triggers = new JSONArray();
        JSONObject trigger = new JSONObject();

        JSONObject idTrigger = new JSONObject();
        idTrigger.put("type", "string");
        idTrigger.put("store", "true");

        JSONObject initTrigger = new JSONObject();
        initTrigger.put("type", "date");
        initTrigger.put("store", "true");

        JSONObject endTrigger = new JSONObject();
        endTrigger.put("type", "date");
        endTrigger.put("store", "true");

        JSONObject descriptionTrigger = new JSONObject();
        descriptionTrigger.put("type", "string");
        descriptionTrigger.put("store", "false");

        trigger.put("id", idTrigger);
        trigger.put("init", initTrigger);
        trigger.put("end", endTrigger);
        trigger.put("description", descriptionTrigger);

        triggers.add(trigger);

        properties.put("id", id);
        properties.put("name", name);
        properties.put("triggers", triggers);

        mapping.put("domain", domain);

        return mapping.toJSONString();
    }

    @Override
    public Class<?> getClassType() {
        return DomainType.class;
    }

    @Override
    public DomainType getDocumentType(Domain document) {
        DomainType domainType = new DomainType();
        if (document.getId() != null) {
            domainType.setIdEntity(document.getId());
        }
        domainType.setName(document.getName());
        domainType.setThematics(document.getThematics());
        return domainType;
    }

    @Override
    public Domain getDocument(DomainType documentType) {
        Domain domain = new Domain();
        domain.setId(documentType.getId());
        domain.setName(documentType.getName());
        domain.setThematics(documentType.getThematics());
        return domain;
    }

}
