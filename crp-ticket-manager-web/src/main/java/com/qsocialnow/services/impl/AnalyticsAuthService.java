package com.qsocialnow.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.config.AnalyticsConfig;

@Service
public class AnalyticsAuthService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsAuthService.class);

    @Autowired
    private AnalyticsConfig analyticsConfig;

	public void redirectAnalyticsHome() {
		
		
	}
 

}
