package com.qsocialnow.services;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.qsocialnow.model.CategoryGroupBySerieIdInput;
import com.qsocialnow.model.CategoryGroupsBySerieIdOuptut;

public interface CategoryGroupsBySerieService {

    @LambdaFunction(functionName = "conjuntosBySerieId")
    CategoryGroupsBySerieIdOuptut conjuntosBySerieId(CategoryGroupBySerieIdInput categoryGroupBySerieIdInput);

}
