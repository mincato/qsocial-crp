package com.qsocialnow.retroactiveprocess.service;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.qsocialnow.retroactiveprocess.model.OrganizationId;
import com.qsocialnow.retroactiveprocess.model.ThematicsByClientOrganizationIdOutput;

public interface ThematicsByOrganizationService {

    @LambdaFunction(functionName = "thematicsByClientOrganizationId")
    ThematicsByClientOrganizationIdOutput thematicsByClientOrganizationId(OrganizationId organizationId);

}
