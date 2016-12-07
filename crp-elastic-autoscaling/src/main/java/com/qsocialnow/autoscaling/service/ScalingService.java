package com.qsocialnow.autoscaling.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticsearch.AWSElasticsearchClient;
import com.qsocialnow.autoscaling.config.AWSElasticsearchConfigurationProvider;

public class ScalingService extends AWSElasticsearchClient {

    private AWSElasticsearchConfigurationProvider configurator;

    public ScalingService(AWSElasticsearchConfigurationProvider configurator) {
        super((AWSCredentialsProvider) configurator);
        this.setRegion(Region.getRegion(Regions.fromName(configurator.getRegion())));
    }
}
