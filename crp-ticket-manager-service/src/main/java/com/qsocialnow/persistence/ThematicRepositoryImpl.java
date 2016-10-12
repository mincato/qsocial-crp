package com.qsocialnow.persistence;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.qsocialnow.common.model.config.CategoryGroup;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.config.AWSLambdaClientConfig;
import com.qsocialnow.model.CategoryGroupBySerieIdInput;
import com.qsocialnow.model.CategoryGroupsBySerieIdOuptut;
import com.qsocialnow.model.OrganizationId;

public class ThematicRepositoryImpl implements ThematicRepository {

    @Autowired
    private AWSLambdaClientConfig awsLambdaClientConfig;

    @Value("${app.client}")
    private Integer clientId;

    @Override
    public List<Thematic> findAll() {
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withCredentials(awsLambdaClientConfig)
                .withRegion(awsLambdaClientConfig.getRegion()).build();

        ThematicsByOrganizationService service = LambdaInvokerFactory.builder().lambdaClient(lambda)
                .build(ThematicsByOrganizationService.class);
        OrganizationId organizationId = new OrganizationId();
        organizationId.setClientOrganizationId(clientId);
        return service.thematicsByClientOrganizationId(organizationId).getThematics();
    }

    @Override
    public List<CategoryGroup> findCategoryGroupsBySerieId(Long thematicId, Long serieId) {
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withCredentials(awsLambdaClientConfig)
                .withRegion(awsLambdaClientConfig.getRegion()).build();

        CategoryGroupsBySerieService service = LambdaInvokerFactory.builder().lambdaClient(lambda)
                .build(CategoryGroupsBySerieService.class);
        CategoryGroupBySerieIdInput categoryGroupInput = new CategoryGroupBySerieIdInput();
        categoryGroupInput.setId(serieId);
        categoryGroupInput.setThematicId(thematicId);
        CategoryGroupsBySerieIdOuptut conjuntosBySerieId = service.conjuntosBySerieId(categoryGroupInput);
        conjuntosBySerieId.getConjuntos().sort(new Comparator<CategoryGroup>() {

            @Override
            public int compare(CategoryGroup o1, CategoryGroup o2) {
                return o1.getNumeroDeOrden().compareTo(o2.getNumeroDeOrden());
            }

        });
        return conjuntosBySerieId.getConjuntos();

    }

}
