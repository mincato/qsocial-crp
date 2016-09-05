package com.qsocialnow.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.services.config.ResolutionService;

@Service
public class ResolutionRepository {

    private Logger log = LoggerFactory.getLogger(ResolutionRepository.class);

    @Autowired
    private ResolutionService resolutionElasticService;

    @Autowired
    private Configurator elasticConfig;

    public Resolution save(String domainId, Resolution newResolution) {
        try {
            String id = resolutionElasticService.indexResolution(elasticConfig, domainId, newResolution);
            newResolution.setId(id);

            return newResolution;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

}
