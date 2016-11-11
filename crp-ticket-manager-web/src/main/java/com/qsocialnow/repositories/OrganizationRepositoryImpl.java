package com.qsocialnow.repositories;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.qsocialnow.common.model.config.ClientOrganization;
import com.qsocialnow.config.AWSLambdaClientConfig;
import com.qsocialnow.config.CacheConfig;

public class OrganizationRepositoryImpl implements OrganizationRepository {

    private static final Logger log = LoggerFactory.getLogger(OrganizationRepositoryImpl.class);

    @Autowired
    private AWSLambdaClientConfig awsLambdaClientConfig;

    @Override
    @Cacheable(value = CacheConfig.ORGANIZATIONS, unless = "#result == null")
    public List<ClientOrganization> findAll() {

        log.info("Searching organization on external service");

        AWSLambda lambda = AWSLambdaClientBuilder.standard().withCredentials(awsLambdaClientConfig)
                .withRegion(awsLambdaClientConfig.getRegion()).build();

        AWSOrganizationService service = LambdaInvokerFactory.builder().lambdaClient(lambda)
                .build(AWSOrganizationService.class);
        return service.clientOrganizations().getClientOrganizations();
    }

}
