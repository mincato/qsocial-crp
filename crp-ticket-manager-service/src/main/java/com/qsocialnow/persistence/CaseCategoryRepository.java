package com.qsocialnow.persistence;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.elasticsearch.services.config.CaseCategoryService;

@Service
public class CaseCategoryRepository implements ReportRepository {

    @Autowired
    private CaseCategoryService caseCategoryElasticService;

    public Map<String, String> findAllReport() {
        return caseCategoryElasticService.findAllAsMap();
    }

}
