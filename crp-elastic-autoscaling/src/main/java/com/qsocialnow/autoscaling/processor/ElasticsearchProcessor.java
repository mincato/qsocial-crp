package com.qsocialnow.autoscaling.processor;

import com.qsocialnow.autoscaling.service.HealthService;

public class ElasticsearchProcessor {

	private HealthService healthService;
	
	public ElasticsearchProcessor(){
	}
	
	public void execute() {
		this.healthService.checkClusterStatus();
    }


	public HealthService getHealthService() {
		return healthService;
	}


	public void setHealthService(HealthService healthService) {
		this.healthService = healthService;
	}
}
