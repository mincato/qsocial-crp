package com.qsocialnow.elasticsearch.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;

public abstract class AWSElasticsearchConfigurationProvider implements AWSCredentials, AWSCredentialsProvider {

    public abstract String getRegion();

    public abstract String getEndpoint();

    public abstract String getEnvironment();
}
