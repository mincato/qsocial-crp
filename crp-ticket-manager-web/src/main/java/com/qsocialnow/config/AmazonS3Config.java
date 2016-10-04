package com.qsocialnow.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;

public class AmazonS3Config implements AWSCredentials, AWSCredentialsProvider {

    private String region;

    private String host;

    private String accessKey;

    private String secretAccessKey;

    private String bucketName;

    @Override
    public String getAWSAccessKeyId() {
        return accessKey;
    }

    @Override
    public String getAWSSecretKey() {
        return secretAccessKey;
    }

    @Override
    public AWSCredentials getCredentials() {
        return this;
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub

    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

}
