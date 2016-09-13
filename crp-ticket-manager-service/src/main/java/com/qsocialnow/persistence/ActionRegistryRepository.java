package com.qsocialnow.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.cases.ActionRegistry;

@Component
public class ActionRegistryRepository {

    private static final Logger log = LoggerFactory.getLogger(ActionRegistryRepository.class);

    public void create(String caseId, ActionRegistry actionRegistry) {
        // TODO guardarlo en Elastic
        log.info("Saving action registry: " + new GsonBuilder().create().toJson(actionRegistry));

    }

}
