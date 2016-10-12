package com.qsocialnow.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;

public class AWSLambdaClientConfig implements AWSCredentials, AWSCredentialsProvider {

    private String region;

    private String accessKey;

    private String secretAccessKey;

    @Override
    public AWSCredentials getCredentials() {
        return this;
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getAWSAccessKeyId() {
        return accessKey;
    }

    @Override
    public String getAWSSecretKey() {
        return secretAccessKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}
