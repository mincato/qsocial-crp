package com.qsocialnow.autoscaling.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.autoscaling.config.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.autoscaling.model.Node;

public class HealthService {

    private static final Logger log = LoggerFactory.getLogger(HealthService.class);

    private AWSElasticsearchConfigurationProvider configurator;

    public HealthService() {

    }

    public void checkClusterStatus() {
        log.info("Starting elastic client");
        ElasticsearchClient client = new ElasticsearchClient(this.configurator);
        client.initClient();
        List<Node> nodes = client.checkClusterHealth();
        client.closeClient();
        
        //check metrics value
        
        //take care autoscaling action based on metrics values 
        
    }

    public void setConfigurator(AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

}