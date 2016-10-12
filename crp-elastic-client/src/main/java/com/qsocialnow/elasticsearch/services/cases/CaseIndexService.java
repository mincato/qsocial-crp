package com.qsocialnow.elasticsearch.services.cases;

import java.time.LocalDateTime;

import com.qsocialnow.elasticsearch.repositories.Repository;

public class CaseIndexService {

    private final static String INDEX_NAME = "cases";

    private static final String ALIAS_QUERY_INDEX = "cases_alias";

    public CaseIndexService() {

    }

    public <T> String getIndex(Repository<T> repository) {
        boolean isCreated = repository.validateIndex(INDEX_NAME);
        // create index
        if (!isCreated) {
            repository.createIndex(INDEX_NAME);
            //updateAlias(repository, indexName);
        }
        return INDEX_NAME;
    }
    
    public <T> String getQueryIndex() {
        return INDEX_NAME;
    }


    private <T> void updateAlias(Repository<T> repository, String index) {
        repository.updateIndexAlias(index, ALIAS_QUERY_INDEX);
    }

    private String generateIndexValue() {
        String indexSufix = null;

        LocalDateTime dateTime = LocalDateTime.now();
        int month = dateTime.getMonthValue();
        int year = dateTime.getYear();
        indexSufix = year + "_" + month;
        return indexSufix;
    }
}
