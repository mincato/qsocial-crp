package com.qsocialnow.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.CategoryGroup;
import com.qsocialnow.common.model.config.Thematic;
import com.qsocialnow.persistence.ThematicRepository;

@Service
public class ThematicService {

	private static final Logger log = LoggerFactory.getLogger(ThematicService.class);
	
    @Autowired
    private ThematicRepository thematicRepository;

    public List<Thematic> findAll() {
    	try {
    		return thematicRepository.findAll();
    	} catch (Exception e) {
    		log.error("There was an error finding thematics", e);
    		throw new RuntimeException(e);
    	}
    }

    public List<CategoryGroup> findCategoryGroupsBySerieId(String thematicId, String serieId) {
        // AWSLambda lambda =
        // AWSLambdaClientBuilder.standard().withCredentials(awsLambdaClientConfig)
        // .withRegion(awsLambdaClientConfig.getRegion()).build();
        //
        // CategoryGroupsBySerieService service =
        // LambdaInvokerFactory.builder().lambdaClient(lambda).build(CategoryGroupsBySerieService.class);
        // CategoryGroupsBySerieIdOuptut conjuntosBySerieId =
        // service.conjuntosBySerieId(categoryGroupInput);
        // conjuntosBySerieId.getConjuntos().sort(new
        // Comparator<CategoryGroup>() {
        //
        // @Override
        // public int compare(CategoryGroup o1, CategoryGroup o2) {
        // return o1.getNumeroDeOrden().compareTo(o2.getNumeroDeOrden());
        // }
        //
        // });
        // return conjuntosBySerieId.getConjuntos();
        return null;
    }

}
