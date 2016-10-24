package com.qsocialnow.persistence;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.elasticsearch.services.config.CaseCategoryService;

@Service
public class CaseCategoryRepository {

    private Logger log = LoggerFactory.getLogger(CaseCategoryRepository.class);

    @Autowired
    private CaseCategoryService caseCategoryElasticService;

    public Map<String, String> findAllReport() {
        return caseCategoryElasticService.findAllAsMap();
    }

}
