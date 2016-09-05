package com.qsocialnow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.persistence.ResolutionRepository;

@Service
public class ResolutionService {

    private static final Logger log = LoggerFactory.getLogger(ResolutionService.class);

    @Autowired
    private ResolutionRepository resolutionRepository;

    public Resolution createResolution(String domainId, Resolution resolution) {
        Resolution resolutionSaved = null;
        try {
            resolutionSaved = resolutionRepository.save(domainId, resolution);
        } catch (Exception e) {
            log.error("There was an error creating resolution: " + resolution.getDescription(), e);
            throw new RuntimeException(e.getMessage());
        }
        return resolutionSaved;
    }

    public Resolution update(String domainId, String resolutionId, Resolution resolution) {
        Resolution resolutionSaved = null;
        try {
            resolution.setId(resolutionId);
            resolutionSaved = resolutionRepository.update(domainId, resolution);
        } catch (Exception e) {
            log.error("There was an error updating resolution: " + resolution.getDescription(), e);
            throw new RuntimeException(e.getMessage());
        }
        return resolutionSaved;
    }

}
