package com.qsocialnow.persistence;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.elasticsearch.services.config.ResolutionService;

@Service
public class ResolutionRepository {

    private Logger log = LoggerFactory.getLogger(ResolutionRepository.class);

    @Autowired
    private ResolutionService resolutionElasticService;

    public Resolution save(String domainId, Resolution newResolution) {
        try {
            String id = resolutionElasticService.indexResolution(domainId, newResolution);
            newResolution.setId(id);

            return newResolution;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public Resolution update(String domainId, Resolution resolution) {
        try {
            String id = resolutionElasticService.updateResolution(domainId, resolution);
            resolution.setId(id);
            return resolution;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public Resolution delete(String domainId, String resolutionId) {
        try {
            resolutionElasticService.deleteResolution(domainId, resolutionId);
            Resolution resolution = new Resolution();
            resolution.setId(resolutionId);
            return resolution;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public Map<String, String> findAllReport() {
        return resolutionElasticService.getAllResolutionsAsMap();
    }

}
