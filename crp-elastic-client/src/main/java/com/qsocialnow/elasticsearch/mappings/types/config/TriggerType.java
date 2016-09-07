package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.Trigger;

import io.searchbox.annotations.JestId;

public class TriggerType extends Trigger {

    @JestId
    private String idTrigger;

    @Override
    public String getId() {
        return this.idTrigger;
    }

}
