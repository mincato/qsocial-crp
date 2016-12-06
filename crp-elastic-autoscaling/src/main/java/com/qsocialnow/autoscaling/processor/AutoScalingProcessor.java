package com.qsocialnow.autoscaling.processor;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.elasticsearch.model.DescribeElasticsearchDomainConfigRequest;
import com.amazonaws.services.elasticsearch.model.DescribeElasticsearchDomainConfigResult;
import com.amazonaws.services.elasticsearch.model.ElasticsearchClusterConfig;
import com.amazonaws.services.elasticsearch.model.ElasticsearchClusterConfigStatus;
import com.amazonaws.services.elasticsearch.model.UpdateElasticsearchDomainConfigRequest;
import com.qsocialnow.autoscaling.model.Mem;
import com.qsocialnow.autoscaling.model.Node;
import com.qsocialnow.autoscaling.model.Total;
import com.qsocialnow.autoscaling.service.HealthService;
import com.qsocialnow.autoscaling.service.ScalingService;

public class AutoScalingProcessor {

    private static final double ALLOWED_MEMORY_SIZE = 60.0;

    private static final int MAX_INSTANCE_COUNT = 20;

    private static final Logger log = LoggerFactory.getLogger(AutoScalingProcessor.class);

    private HealthService healthService;

    private ScalingService amazonClient;

    public AutoScalingProcessor() {
    }

    public void execute() {
        List<Node> nodes = this.healthService.checkClusterStatus();
        if (!isValidNodeStatus(nodes)) {

            DescribeElasticsearchDomainConfigResult result = amazonClient.describeElasticsearchDomainConfig(this
                    .createDescribeConfigRequest());

            if (result != null && result.getDomainConfig() != null) {

                log.info(result.getDomainConfig().toString());
                ElasticsearchClusterConfigStatus currentConfiguration = result.getDomainConfig()
                        .getElasticsearchClusterConfig();
                // UpdateElasticsearchDomainConfigResult scalingResult =
                // amazonClient.updateElasticsearchDomainConfig(this.createUpdateRequest(currentConfiguration));

            } else {
                log.info("No configuration values retrieved to update");
            }
        }
    }

    private DescribeElasticsearchDomainConfigRequest createDescribeConfigRequest() {
        DescribeElasticsearchDomainConfigRequest request = new DescribeElasticsearchDomainConfigRequest();
        request.setDomainName("prc-cases");
        return request;
    }

    private UpdateElasticsearchDomainConfigRequest createUpdateRequest(
            ElasticsearchClusterConfigStatus currentConfiguration) {
        UpdateElasticsearchDomainConfigRequest updateElasticsearchDomainConfigRequest = new UpdateElasticsearchDomainConfigRequest();
        updateElasticsearchDomainConfigRequest.setDomainName("prc-cases");
        ElasticsearchClusterConfig elasticsearchClusterConfig = new ElasticsearchClusterConfig();
        setConfiguration(elasticsearchClusterConfig, currentConfiguration);
        updateElasticsearchDomainConfigRequest.setElasticsearchClusterConfig(elasticsearchClusterConfig);

        return updateElasticsearchDomainConfigRequest;
    }

    private void setConfiguration(ElasticsearchClusterConfig elasticsearchClusterConfig,
            ElasticsearchClusterConfigStatus currentConfigurationStatus) {

        ElasticsearchClusterConfig currentConfig = currentConfigurationStatus.getOptions();
        if (currentConfig != null) {
            int instanceCount = currentConfig.getInstanceCount();
            log.info("Cluster configuration: Instance count: " + instanceCount);
            if (instanceCount < MAX_INSTANCE_COUNT) {
                // updating even instance count
                elasticsearchClusterConfig.setInstanceCount(instanceCount += 2);
                elasticsearchClusterConfig.setDedicatedMasterCount(currentConfig.getDedicatedMasterCount());
            }

        }

    }

    private boolean isValidNodeStatus(List<Node> nodes) {
        if (nodes != null) {
            List<Node> criticalNodes = nodes
                    .stream()
                    .filter(u -> u.getFs() != null && u.getFs().getTotal() != null
                            && isValidThreashold(u.getFs().getTotal())).collect(Collectors.toList());
            if (criticalNodes != null && !criticalNodes.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidThreashold(Total total) {

        double consumed = (total.getFreeInBytes() / total.getTotalInBytes()) * 100;
        log.info("Node:" + total.toString() + "   - total% consumed: " + consumed);

        return consumed > ALLOWED_MEMORY_SIZE;
    }

    public HealthService getHealthService() {
        return healthService;
    }

    public void setHealthService(HealthService healthService) {
        this.healthService = healthService;
    }

    public ScalingService getAmazonClient() {
        return amazonClient;
    }

    public void setAmazonClient(ScalingService amazonClient) {
        this.amazonClient = amazonClient;
    }
}
