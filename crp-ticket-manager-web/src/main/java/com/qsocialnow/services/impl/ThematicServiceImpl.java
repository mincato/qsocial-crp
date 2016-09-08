package com.qsocialnow.services.impl;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.qsocialnow.model.OrganizationId;
import com.qsocialnow.model.Thematic;
import com.qsocialnow.services.ThematicService;
import com.qsocialnow.services.ThematicsByOrganizationService;

@Service("thematicService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ThematicServiceImpl implements ThematicService {

    @Override
    public List<Thematic> findAll() {
        AWSLambdaClient lambda = new AWSLambdaClient();
        lambda.configureRegion(Regions.US_WEST_2);

        ThematicsByOrganizationService service = LambdaInvokerFactory.build(ThematicsByOrganizationService.class,
                lambda);
        OrganizationId organizationId = new OrganizationId();
        // TODO agregar organizacion id cunado integremos la organizacion
        // cliente.
        return service.thematicsByClientOrganizationId(organizationId).getThematics();
    }

}
