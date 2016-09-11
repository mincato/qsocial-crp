package com.qsocialnow.elasticsearch.configuration;

import com.amazonaws.auth.AWSCredentials;

public class CaseConfigurator extends AWSElasticsearchConfigurationProvider {

    public static final String SERVICE_NAME = "es";

    private final String region;

    private final String host;

    private final String environment;

    private static final String ENDPOINT_ROOT = "https://";

    private static final String PATH = "/";

    private final String accessKey;
    private final String secretAccessKey;

    public CaseConfigurator() {
        this.host = "search-qsocial-cases-xndgjucmwdw5ychpiiqo5fn4bu.us-west-2.es.amazonaws.com";
        this.region = "us-west-2";
        this.accessKey = "AKIAINTJDMMOSA6AQ2NQ";
        this.secretAccessKey = "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";
        this.environment = "dev";
    }

    public CaseConfigurator(final String host, final String region, final String acccesKey,
            final String secretAccessKey, final String environment) {
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
}
