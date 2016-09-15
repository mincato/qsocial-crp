package com.qsocialnow.eventresolver.action;

import java.util.List;

import com.qsocialnow.eventresolver.processor.ExecutionMessageRequest;

public interface Action<Input, Output> {

    Output execute(Input inputElement, List<String> parameters, ExecutionMessageRequest request);

    Output execute(Input inputElement, Output outputElement, List<String> parameters);

}
