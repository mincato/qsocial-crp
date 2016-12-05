package com.qsocialnow.autoscaling.processor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.elasticsearch.model.DescribeElasticsearchDomainConfigRequest;
import com.amazonaws.services.elasticsearch.model.DescribeElasticsearchDomainConfigResult;
import com.amazonaws.services.elasticsearch.model.ElasticsearchClusterConfig;
import com.amazonaws.services.elasticsearch.model.UpdateElasticsearchDomainConfigRequest;
import com.qsocialnow.autoscaling.model.Node;
import com.qsocialnow.autoscaling.service.AmazonElasticsearchServiceClient;
import com.qsocialnow.autoscaling.service.ElasticsearchClient;
import com.qsocialnow.autoscaling.service.HealthService;

public class AutoScalingProcessor {

    private static final Logger log = LoggerFactory.getLogger(AutoScalingProcessor.class);

    private HealthService healthService;

    private AmazonElasticsearchServiceClient amazonClient;

    public AutoScalingProcessor() {
    }

    public void execute() {
        List<Node> nodes = this.healthService.checkClusterStatus();

        if (!validateAlarms(nodes)) {

            DescribeElasticsearchDomainConfigResult result = amazonClient.describeElasticsearchDomainConfig(this
                    .createDescribeConfigRequest());
            // amazonClient.updateElasticsearchDomainConfig(this.createUpdateRequest());

            log.info(result.getDomainConfig().toString());
        }
    }

    private DescribeElasticsearchDomainConfigRequest createDescribeConfigRequest() {
        DescribeElasticsearchDomainConfigRequest request = new DescribeElasticsearchDomainConfigRequest();

        request.setDomainName("prc-cases");

        return request;
    }

    private UpdateElasticsearchDomainConfigRequest createUpdateRequest() {
        UpdateElasticsearchDomainConfigRequest updateElasticsearchDomainConfigRequest = new UpdateElasticsearchDomainConfigRequest();
        updateElasticsearchDomainConfigRequest.setDomainName("prc-cases");
        ElasticsearchClusterConfig elasticsearchClusterConfig = new ElasticsearchClusterConfig();

        updateElasticsearchDomainConfigRequest.setElasticsearchClusterConfig(elasticsearchClusterConfig);

        return updateElasticsearchDomainConfigRequest;
    }

    private boolean validateAlarms(List<Node> nodes) {
        // TODO Auto-generated method stub
        return false;
    }

    public HealthService getHealthService() {
        return healthService;
    }

    public void setHealthService(HealthService healthService) {
        this.healthService = healthService;
    }

    public AmazonElasticsearchServiceClient getAmazonClient() {
        return amazonClient;
    }

    public void setAmazonClient(AmazonElasticsearchServiceClient amazonClient) {
        this.amazonClient = amazonClient;
    }
}
