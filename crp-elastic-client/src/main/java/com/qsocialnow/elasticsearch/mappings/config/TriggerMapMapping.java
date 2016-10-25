package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.TriggerType;

public class TriggerMapMapping implements Mapping<TriggerType, Trigger> {

    public static final String TYPE = "trigger";

    private static TriggerMapMapping instance;

    private final String index;

    private TriggerMapMapping(String index) {
        this.index = index;
    }

    public static TriggerMapMapping getInstance(String index) {
        if (instance == null)
            instance = new TriggerMapMapping(index);
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
        triggerType.setName(document.getName());
        return triggerType;
    }

    @Override
    public Trigger getDocument(TriggerType documentType) {
        Trigger trigger = new Trigger();
        trigger.setId(documentType.getId());
        trigger.setName(documentType.getName());
        return trigger;
    }

}
