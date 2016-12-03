package com.qsocialnow.autoscaling.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.autoscaling.config.AWSElasticsearchConfigurationProvider;

public class HealthService {

    private static final Logger log = LoggerFactory.getLogger(HealthService.class);
    
    private AWSElasticsearchConfigurationProvider configurator;
    
    public HealthService(){

    }
    

    public void checkClusterStatus() {
        log.info("Starting elastic client");
    	ElasticsearchClient client = new ElasticsearchClient(this.configurator);
        client.initClient();
        client.checkClusterHealth();
    }

    public void setConfigurator(AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

}