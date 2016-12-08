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
import com.amazonaws.services.elasticsearch.model.UpdateElasticsearchDomainConfigResult;
import com.qsocialnow.autoscaling.model.Docs;
import com.qsocialnow.autoscaling.model.Fs;
import com.qsocialnow.autoscaling.model.Indices;
import com.qsocialnow.autoscaling.model.Mem;
import com.qsocialnow.autoscaling.model.Node;
import com.qsocialnow.autoscaling.model.Total;
import com.qsocialnow.autoscaling.service.HealthService;
import com.qsocialnow.autoscaling.service.ScalingService;

public class AutoScalingProcessor {

    private static final String PRC_CASES_DOMAIN_NAME = "prc-cases";

    private static final String NODE_CONFIGURATION_ACTIVE_VALUE = "active";

    private static final double ALLOWED_MEMORY_SIZE = 80.0;

    private static final int MAX_INSTANCE_COUNT = 20;

    private static final Logger log = LoggerFactory.getLogger(AutoScalingProcessor.class);

    private HealthService healthService;

    private ScalingService amazonClient;

    public AutoScalingProcessor() {
    }

    public void execute() {

        DescribeElasticsearchDomainConfigResult result = amazonClient.describeElasticsearchDomainConfig(this
                .createDescribeConfigRequest());

        if (validateClusterStatus(result)) {

            List<Node> nodes = this.healthService.checkClusterStatus();
            if (!isValidNodeStatus(nodes)) {
                if (result != null && result.getDomainConfig() != null) {

                    ElasticsearchClusterConfigStatus currentConfiguration = result.getDomainConfig()
                            .getElasticsearchClusterConfig();

                    if (currentConfiguration != null) {
                        ElasticsearchClusterConfig currentConfig = currentConfiguration.getOptions();
                        if (currentConfig != null) {
                            int instanceCount = currentConfig.getInstanceCount();
                            log.info("Cluster configuration: Instance count: " + instanceCount);
                            if (instanceCount < MAX_INSTANCE_COUNT) {
                                UpdateElasticsearchDomainConfigResult scalingResult = amazonClient
                                        .updateElasticsearchDomainConfig(this.createUpdateRequest(currentConfiguration));
                            } else {
                                log.info("Instance count retrieved max allowed value - Autoscaling action not executed");
                            }
                        }
                    }
                } else {
                    log.info("No configuration values retrieved to update");
                }
            }
        } else {
            log.info("Cluster status no active...waiting pending status!");
        }
    }

    private boolean validateClusterStatus(DescribeElasticsearchDomainConfigResult result) {
        if (result.getDomainConfig() != null && result.getDomainConfig().getElasticsearchClusterConfig() != null
                && result.getDomainConfig().getElasticsearchClusterConfig().getStatus() != null) {
            String state = result.getDomainConfig().getElasticsearchClusterConfig().getStatus().getState();
            log.info("Node configuration is " + state);
            if (state.toLowerCase().equals(NODE_CONFIGURATION_ACTIVE_VALUE)) {
                return true;
            }

        }
        return false;
    }

    private DescribeElasticsearchDomainConfigRequest createDescribeConfigRequest() {
        DescribeElasticsearchDomainConfigRequest request = new DescribeElasticsearchDomainConfigRequest();
        request.setDomainName(PRC_CASES_DOMAIN_NAME);
        return request;
    }

    private UpdateElasticsearchDomainConfigRequest createUpdateRequest(
            ElasticsearchClusterConfigStatus currentConfiguration) {
        UpdateElasticsearchDomainConfigRequest updateElasticsearchDomainConfigRequest = new UpdateElasticsearchDomainConfigRequest();
        updateElasticsearchDomainConfigRequest.setDomainName(PRC_CASES_DOMAIN_NAME);
        ElasticsearchClusterConfig elasticsearchClusterConfig = new ElasticsearchClusterConfig();
        setConfiguration(elasticsearchClusterConfig, currentConfiguration);
        updateElasticsearchDomainConfigRequest.setElasticsearchClusterConfig(elasticsearchClusterConfig);

        return updateElasticsearchDomainConfigRequest;
    }

    private void setConfiguration(ElasticsearchClusterConfig elasticsearchClusterConfig,
            ElasticsearchClusterConfigStatus currentConfigurationStatus) {

        ElasticsearchClusterConfig currentConfig = currentConfigurationStatus.getOptions();
        int instanceCount = currentConfig.getInstanceCount();
        // updating even instance count
        elasticsearchClusterConfig.setInstanceCount(instanceCount += 2);
        elasticsearchClusterConfig.setDedicatedMasterCount(currentConfig.getDedicatedMasterCount());
    }

    private boolean isValidNodeStatus(List<Node> nodes) {
        if (nodes != null) {
            List<Node> criticalNodes = nodes.stream()
                    .filter(u -> isNotDedicatedMasterNode(u.getIndices()) && !isValidFSFreeMemory(u.getFs()))
                    .collect(Collectors.toList());
            if (criticalNodes != null && !criticalNodes.isEmpty()) {
                log.info(criticalNodes.size()
                        + " data Nodes are over allowed memory consumption - Starting process to autoscaling");
                return false;
            }
        }
        return true;
    }

    private boolean isNotDedicatedMasterNode(Indices indices) {
        if (indices != null && indices.getDocs() != null) {
            Docs docs = indices.getDocs();
            if (docs.getCount() > 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidFSFreeMemory(Fs fs) {
        if (fs != null && fs.getTotal() != null) {
            Total total = fs.getTotal();
            double consumed = (total.getFreeInBytes() * 100.0f) / total.getTotalInBytes();
            log.info("Data Node: free: " + total.getFreeInBytes() + " total: " + total.getTotalInBytes()
                    + " - total% consumed: " + consumed);
            return consumed < ALLOWED_MEMORY_SIZE;
        } else
            return true;
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
