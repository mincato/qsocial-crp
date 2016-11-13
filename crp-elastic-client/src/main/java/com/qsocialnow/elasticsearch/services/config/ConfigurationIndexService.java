package com.qsocialnow.elasticsearch.services.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.mappings.config.CaseCategoryMapping;
import com.qsocialnow.elasticsearch.mappings.config.DomainMapping;
import com.qsocialnow.elasticsearch.mappings.config.ResolutionMapping;
import com.qsocialnow.elasticsearch.mappings.config.SegmentMapping;
import com.qsocialnow.elasticsearch.mappings.config.SubjectCategoryMapping;
import com.qsocialnow.elasticsearch.mappings.config.TriggerMapping;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;

public class ConfigurationIndexService {

    private static final Logger log = LoggerFactory.getLogger(ConfigurationIndexService.class);

    private static final String[] MAPPING_TYPES = { DomainMapping.TYPE, ResolutionMapping.TYPE, SegmentMapping.TYPE,
            TriggerMapping.TYPE, CaseCategoryMapping.TYPE, SubjectCategoryMapping.TYPE };

    private String indexName;

    private Configurator configurator;

    public ConfigurationIndexService() {

    }

    @SuppressWarnings("rawtypes")
    public void initIndex() {
        RepositoryFactory esfactory = new RepositoryFactory(configurator);
        Repository repository = esfactory.initManager();
        repository.initClient();

        boolean isCreated = repository.validateIndex(indexName);
        // create index
        if (!isCreated) {
            repository.createIndex(indexName, null);
            createMappings(repository, indexName);
        }
    }

    public String getIndexName() {
        return indexName;
    }

    @SuppressWarnings("rawtypes")
    private void createMappings(Repository repository, String index) {
        try {

            for (int i = 0; i < MAPPING_TYPES.length; i++) {
                InputStream in = getClass().getResourceAsStream("/mappings/" + MAPPING_TYPES[i] + ".json");
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

    public void setConfigurator(Configurator configurator) {
        this.configurator = configurator;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

}
