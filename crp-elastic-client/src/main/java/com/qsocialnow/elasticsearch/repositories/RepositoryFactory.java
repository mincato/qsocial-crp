package com.qsocialnow.elasticsearch.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;

public class RepositoryFactory<T> {

    private static final Logger log = LoggerFactory.getLogger(RepositoryFactory.class);

    private AWSElasticsearchConfigurationProvider configurator;

    public RepositoryFactory(final AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

    public Repository<T> initManager() {
        log.info("Creating manager to handle ES service:");

        Repository<T> manager = null;

        switch (this.configurator.getEnvironment()) {
            case "dev":
                manager = new ElasticsearchRepository<T>(this.configurator);
                break;

            default:
                break;
        }

        return manager;
    }

}
