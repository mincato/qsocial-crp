package com.qsocialnow.elasticsearch.services;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.mappings.config.DetectionCriteriaMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.DetectionCriteriaType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;

public class DetectionCriteriaService {

    public String indexDetectionCriteria(DetectionCriteria detectionCriteria) {
        Configurator configurator = new Configurator();
        RepositoryFactory<DetectionCriteriaType> esfactory = new RepositoryFactory<DetectionCriteriaType>(configurator);

        Repository<DetectionCriteriaType> repository = esfactory.initManager();
        repository.initClient();

        DetectionCriteriaMapping mapping = DetectionCriteriaMapping.getInstance();
        DetectionCriteriaType document = mapping.getDocumentType(detectionCriteria);

        String response = repository.indexMapping(mapping, document);
        repository.closeClient();

        return response;
    }

}
