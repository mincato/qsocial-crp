package com.qsocialnow.persistence;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.qsocialnow.model.OrganizationId;
import com.qsocialnow.model.ThematicsByClientOrganizationIdOutput;

public interface ThematicsByOrganizationService {

    @LambdaFunction(functionName = "thematicsByClientOrganizationId")
    ThematicsByClientOrganizationIdOutput thematicsByClientOrganizationId(OrganizationId organizationId);

}
