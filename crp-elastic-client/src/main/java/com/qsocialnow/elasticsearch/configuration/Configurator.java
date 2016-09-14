package com.qsocialnow.elasticsearch.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.qsocialnow.common.config.RefreshableConfig;

public class Configurator extends AWSElasticsearchConfigurationProvider implements RefreshableConfig<Configurator> {

    public static final String SERVICE_NAME = "es";

    private String region;

    private String host;

    private String environment;

    private static final String ENDPOINT_ROOT = "https://";

    private static final String PATH = "/";

    private String accessKey;
    private String secretAccessKey;

    public Configurator() {
        this.host = "search-qsocial-config-ls2uikgtez6zcbjmfmwtwfuboi.us-west-2.es.amazonaws.com";
        this.region = "us-west-2";
        this.accessKey = "AKIAIS4SU5EZLVOYE3GA";
        this.secretAccessKey = "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";
        this.environment = "dev";

    }

    public Configurator(final String host, final String region, final String acccesKey, final String secretAccessKey,
            final String environment) {
        this.host = host;
        this.region = region;
        this.accessKey = acccesKey;
        this.secretAccessKey = secretAccessKey;
        this.environment = environment;
    }

    public String getEndpoint() {
        return ENDPOINT_ROOT + this.host + PATH;
    }

    public String getRegion() {
        return this.region;
    }

    @Override
    public String getAWSAccessKeyId() {
        return this.accessKey;
    }

    @Override
    public String getAWSSecretKey() {
        return this.secretAccessKey;
    }

    public String getEnvironment() {
        return this.environment;
    }

    @Override
    public AWSCredentials getCredentials() {
        return this;
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

    public void refresh(Configurator newConfig) {
        this.host = newConfig.host;
        this.region = newConfig.region;
        this.accessKey = newConfig.accessKey;
        this.secretAccessKey = newConfig.secretAccessKey;
        this.environment = newConfig.environment;
    }
}
