package com.qsocialnow.eventresolver.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpsertCaseStrategySelector {

    @Autowired
    private ResponseDetectorUpsertCaseStrategy responseDetectorUpsertCaseStrategy;

    @Autowired
    private DefaultUpsertCaseStrategy defaultUpsertCaseStrategy;

    public UpsertCaseStrategy select(ExecutionMessageRequest request) {
        if (request.getInput().isResponseDetected()) {
            return responseDetectorUpsertCaseStrategy;
        }
        return defaultUpsertCaseStrategy;
    }

}
