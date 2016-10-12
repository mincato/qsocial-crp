package com.qsocialnow.persistence;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.qsocialnow.common.model.config.CategoryGroup;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.config.AWSLambdaClientConfig;
import com.qsocialnow.config.CacheConfig;
import com.qsocialnow.model.CategoryGroupBySerieIdInput;
import com.qsocialnow.model.CategoryGroupsBySerieIdOuptut;
import com.qsocialnow.model.OrganizationId;

public class ThematicRepositoryImpl implements ThematicRepository {

    private static final Logger log = LoggerFactory.getLogger(ThematicRepositoryImpl.class);

    @Autowired
    private AWSLambdaClientConfig awsLambdaClientConfig;

    @Value("${app.client}")
    private Integer clientId;

    @Override
    @Cacheable(value = CacheConfig.THEMATICS, unless = "#result == null")
    public List<Thematic> findAll() {

        log.info("Searching thematics on external service");

        AWSLambda lambda = AWSLambdaClientBuilder.standard().withCredentials(awsLambdaClientConfig)
                .withRegion(awsLambdaClientConfig.getRegion()).build();

        ThematicsByOrganizationService service = LambdaInvokerFactory.builder().lambdaClient(lambda)
                .build(ThematicsByOrganizationService.class);
        OrganizationId organizationId = new OrganizationId();
        organizationId.setClientOrganizationId(clientId);
        return service.thematicsByClientOrganizationId(organizationId).getThematics();
    }

    @Override
    @Cacheable(value = CacheConfig.CATEGORIES, unless = "#result == null")
    public List<CategoryGroup> findCategoryGroupsBySerieId(Long thematicId, Long serieId) {

        log.info("Searching categories on external service");

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
                if (o1.getNumeroDeOrden() != null && o2.getNumeroDeOrden() != null) {
                    return o1.getNumeroDeOrden().compareTo(o2.getNumeroDeOrden());
                } else if (o1.getNumeroDeOrden() != null) {
                    return -1;
                } else if (o2.getNumeroDeOrden() != null) {
                    return 1;
                } else {
                    return 0;
                }
            }

        });
        return conjuntosBySerieId.getConjuntos();

    }

}
