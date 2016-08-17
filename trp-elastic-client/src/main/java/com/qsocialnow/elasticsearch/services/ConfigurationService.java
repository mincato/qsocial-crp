package com.qsocialnow.elasticsearch.services;

import com.qsocialnow.elasticsearch.configuration.CaseConfigurator;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.repositories.ElasticsearchRepository;

public class ConfigurationService {

    public void createMapping(Mapping mapping) {

        Configurator configurator = new Configurator();
        ElasticsearchRepository repository = new ElasticsearchRepository(configurator);
        repository.initClient();
        repository.createMapping(mapping);

    }

    public void createIndex(String index) {

        Configurator configurator = new Configurator();
        ElasticsearchRepository repository = new ElasticsearchRepository(configurator);
        repository.initClient();
        repository.createIndex(index);
        repository.closeClient();
    }

    public void deleteIndex(String index) {
        Configurator configurator = new Configurator();
        ElasticsearchRepository repository = new ElasticsearchRepository(configurator);
        repository.initClient();
        repository.deleteIndex(index);

    }

    public void deleteCaseIndex(String index) {
        CaseConfigurator configurator = new CaseConfigurator();
        ElasticsearchRepository repository = new ElasticsearchRepository(configurator);
        repository.initClient();
        repository.deleteIndex(index);

    }
}
