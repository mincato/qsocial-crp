package com.qsocialnow.elasticsearch.services.cases;

import java.time.LocalDateTime;

import com.qsocialnow.elasticsearch.repositories.Repository;

public class DynamicIndexService{
	
	private final static String INDEX_NAME = "cases_";
	
	private static final String ALIAS_QUERY_INDEX = "cases_alias";

	public DynamicIndexService() {

    }
	 
	public <T> String getIndex(Repository<T> repository){
		String indexName = INDEX_NAME + generateIndexValue();
		boolean isCreated = repository.validateIndex(indexName);
        // create index
        if (!isCreated) {
            repository.createIndex(indexName);
            updateAlias(repository,indexName);
        }return indexName;
	} 
	
	private <T> void updateAlias(Repository<T> repository,String index) {
		repository.updateIndexAlias(index, ALIAS_QUERY_INDEX);
	}

	public <T> String getQueryIndex(){
		return ALIAS_QUERY_INDEX;
	}
	
	private String generateIndexValue() {
        String indexSufix = null;

        LocalDateTime dateTime = LocalDateTime.now();
        int month = dateTime.getMonthValue();
        int year = dateTime.getYear();
        int hour = dateTime.getHour();
        indexSufix = year + "_" + month+ "_" +hour;
        return indexSufix;
	}
}
