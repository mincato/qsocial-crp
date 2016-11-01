package com.qsocialnow.retroactiveprocess.service;

import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.qsocialnow.retroactiveprocess.config.AWSLambdaClientConfig;
import com.qsocialnow.retroactiveprocess.model.OrganizationId;
import com.qsocialnow.retroactiveprocess.model.RealTimeStaticServerByThematic;
import com.qsocialnow.retroactiveprocess.model.ThematicsByClientOrganizationIdOutput;

public class EventsPagedConfigService {

    private static final Logger log = LoggerFactory.getLogger(EventsPagedConfigService.class);

    @Autowired
    private AWSLambdaClientConfig awsLambdaClientConfig;

    @Value("${app.client}")
    private Integer clientId;

    public String findRealtimeStaticServerByThematic(Long thematic) {

        log.info("Searching thematics on external service");

        AWSLambda lambda = AWSLambdaClientBuilder.standard().withCredentials(awsLambdaClientConfig)
                .withRegion(awsLambdaClientConfig.getRegion()).build();

        ThematicsByOrganizationService service = LambdaInvokerFactory.builder().lambdaClient(lambda)
                .build(ThematicsByOrganizationService.class);
        OrganizationId organizationId = new OrganizationId();
        organizationId.setClientOrganizationId(clientId);
        String server = "";
        ThematicsByClientOrganizationIdOutput thematicsByClientOrganizationId = service
                .thematicsByClientOrganizationId(organizationId);
        if (thematicsByClientOrganizationId != null
                && CollectionUtils.isNotEmpty(thematicsByClientOrganizationId.getThematics())) {
            Optional<RealTimeStaticServerByThematic> optional = thematicsByClientOrganizationId.getThematics().stream()
                    .filter(realTimeStaticServer -> realTimeStaticServer.getId().equals(thematic)).findFirst();
            if (optional.isPresent()) {
                server = optional.get().getRealTimeStatisticsServer();
            }
        }
        return server;
    }

}
