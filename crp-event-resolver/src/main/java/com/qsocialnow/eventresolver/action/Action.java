package com.qsocialnow.eventresolver.action;

import java.util.List;

public interface Action<Input, Output> {

    Output execute(Input inputElement, List<String> parameters);

    Output execute(Input inputElement, Output outputElement, List<String> parameters);

}
