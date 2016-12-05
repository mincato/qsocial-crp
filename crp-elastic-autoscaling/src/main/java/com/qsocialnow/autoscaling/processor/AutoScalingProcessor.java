package com.qsocialnow.autoscaling.processor;

import java.util.List;

import com.amazonaws.services.elasticsearch.model.UpdateElasticsearchDomainConfigRequest;
import com.qsocialnow.autoscaling.model.Node;
import com.qsocialnow.autoscaling.service.AmazonElasticsearchServiceClient;
import com.qsocialnow.autoscaling.service.HealthService;

public class AutoScalingProcessor {

    private HealthService healthService;
    
    private AmazonElasticsearchServiceClient amazonClient;

    public AutoScalingProcessor() {
    }

    public void execute() {
        List<Node> nodes = this.healthService.checkClusterStatus();
        
        if(!validateAlarms(nodes)){
        	amazonClient.updateElasticsearchDomainConfig(this.createUpdateRequest());     	
        }
    }

    
    private UpdateElasticsearchDomainConfigRequest createUpdateRequest() {
    	UpdateElasticsearchDomainConfigRequest updateElasticsearchDomainConfigRequest = new UpdateElasticsearchDomainConfigRequest();    	
    	updateElasticsearchDomainConfigRequest.setDomainName(domainName);
    	
    	
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
