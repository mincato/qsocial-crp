package com.qsocialnow.persistence;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.qsocialnow.model.OrganizationId;
import com.qsocialnow.model.UsersByClientOrganizationIdOutput;

public interface UsersByOrganizationService {

    @LambdaFunction(functionName = "usersByClientOrganizationId")
    UsersByClientOrganizationIdOutput usersByClientOrganizationId(OrganizationId organizationId);

}
