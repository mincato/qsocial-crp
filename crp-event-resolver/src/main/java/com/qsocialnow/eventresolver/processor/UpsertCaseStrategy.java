package com.qsocialnow.eventresolver.processor;

import com.qsocialnow.common.model.cases.Case;

public interface UpsertCaseStrategy {

    Case upsert(ExecutionMessageRequest request);

}
