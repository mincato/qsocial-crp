package com.qsocialnow.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.config.AWSLambdaClientConfig;
import com.qsocialnow.model.OrganizationId;

public class ThematicRepositoryImpl implements ThematicRepository {

    @Autowired
    private AWSLambdaClientConfig awsLambdaClientConfig;

    @Value("app.client")
    private Integer clientId;

    @Override
    public List<Thematic> findAll() {
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withCredentials(awsLambdaClientConfig)
                .withRegion(awsLambdaClientConfig.getRegion()).build();

        ThematicsByOrganizationService service = LambdaInvokerFactory.build(ThematicsByOrganizationService.class,
                lambda);
        OrganizationId organizationId = new OrganizationId();
        organizationId.setClientOrganizationId(clientId);
        return service.thematicsByClientOrganizationId(organizationId).getThematics();
    }

}
