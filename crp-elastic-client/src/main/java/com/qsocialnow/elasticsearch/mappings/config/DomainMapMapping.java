package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.DomainType;

public class DomainMapMapping implements Mapping<DomainType, Domain> {

    public static final String TYPE = "domain";

    private static DomainMapMapping instance;

    private final String index;

    private DomainMapMapping(String index) {
        this.index = index;
    }

    public static DomainMapMapping getInstance(String index) {
        if (instance == null)
            instance = new DomainMapMapping(index);
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

        properties.put("id", id);
        properties.put("name", name);

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
        return domainType;
    }

    @Override
    public Domain getDocument(DomainType documentType) {
        Domain domain = new Domain();
        domain.setId(documentType.getId());
        domain.setName(documentType.getName());
        return domain;
    }

}
