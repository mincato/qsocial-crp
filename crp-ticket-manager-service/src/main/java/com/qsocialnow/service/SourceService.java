package com.qsocialnow.service;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceService {

    private static final Logger log = LoggerFactory.getLogger(SourceService.class);

    @Autowired
    private com.qsocialnow.common.services.SourceService sourceCommonService;

    public Set<Long> getBlockedSources() {
        try {
            return sourceCommonService.getBlockedSources();
        } catch (Exception e) {
            log.error("There was an error retrieving blocked sources", e);
            throw new RuntimeException(e);
        }
    }

}
