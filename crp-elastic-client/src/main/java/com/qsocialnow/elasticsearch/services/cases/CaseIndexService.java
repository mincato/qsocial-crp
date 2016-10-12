package com.qsocialnow.elasticsearch.services.cases;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.qsocialnow.elasticsearch.repositories.Repository;


public class CaseIndexService {

    private static final Logger log = LoggerFactory.getLogger(CaseIndexService.class);
	
	private final static String INDEX_NAME = "cases";

    private static final String ALIAS_QUERY_INDEX = "cases_alias";

	private static final String[] MAPPING_TYPES = {"case"};
	
    public CaseIndexService() {

    }

    public <T> String getIndex(Repository<T> repository) {
        boolean isCreated = repository.validateIndex(INDEX_NAME);
        // create index
        if (!isCreated) {
            repository.createIndex(INDEX_NAME);
            //updateAlias(repository, indexName);
            //createMappings(repository, INDEX_NAME);
        }
        return INDEX_NAME;
    }

    public <T> String getQueryIndex() {
        return INDEX_NAME;
    }

    private <T> void createMappings(Repository<T> repository, String index) {
        try {

        	for (int i = 0; i < MAPPING_TYPES.length; i++) {
        		InputStream in = getClass().getResourceAsStream("/mappings/"+MAPPING_TYPES[i]+".json");
            	BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
                log.info("adding mapping: " + jsonObject.toJSONString());

                repository.createMapping(index, MAPPING_TYPES[i], jsonObject.toJSONString());
                reader.close();
                in.close();
            }

        } catch (FileNotFoundException e) {
            log.error("Error Mapping File Not found: ", e);
        } catch (IOException e) {
            log.error("Error IOException: ", e);
        } catch (ParseException e) {
            log.error("Error parsing mapping definition: ", e);
        }
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