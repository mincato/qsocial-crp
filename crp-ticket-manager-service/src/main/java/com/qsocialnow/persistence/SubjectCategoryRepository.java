package com.qsocialnow.persistence;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.elasticsearch.services.config.CaseCategoryService;
import com.qsocialnow.elasticsearch.services.config.SubjectCategoryService;

@Service
public class SubjectCategoryRepository {

    private Logger log = LoggerFactory.getLogger(SubjectCategoryRepository.class);

    @Autowired
    private SubjectCategoryService subjectCategoryElasticService;

    public Map<String, String> findAllReport() {
        return subjectCategoryElasticService.findAllAsMap();
    }

}
