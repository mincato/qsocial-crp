package com.qsocialnow.services.impl;

import java.util.Comparator;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.qsocialnow.model.CategoryGroup;
import com.qsocialnow.model.CategoryGroupBySerieIdInput;
import com.qsocialnow.model.CategoryGroupsBySerieIdOuptut;
import com.qsocialnow.services.CategoryGroupsBySerieService;
import com.qsocialnow.services.CategoryService;

@Service("categoryService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CategoryServiceImpl implements CategoryService {

    @Override
    public List<CategoryGroup> findBySerieId(CategoryGroupBySerieIdInput categoryGroupInput) {
        AWSLambdaClient lambda = new AWSLambdaClient();
        lambda.configureRegion(Regions.US_WEST_2);

        CategoryGroupsBySerieService service = LambdaInvokerFactory.build(CategoryGroupsBySerieService.class, lambda);
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
