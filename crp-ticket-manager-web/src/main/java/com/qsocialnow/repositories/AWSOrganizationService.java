package com.qsocialnow.repositories;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface AWSOrganizationService {

    @LambdaFunction(functionName = "clientOrganizations")
    ClientOrganizationsOutput clientOrganizations();

}
