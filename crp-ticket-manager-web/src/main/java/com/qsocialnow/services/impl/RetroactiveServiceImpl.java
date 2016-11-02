package com.qsocialnow.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.qsocialnow.common.model.retroactive.RetroactiveProcess;
import com.qsocialnow.common.model.retroactive.RetroactiveProcessRequest;
import com.qsocialnow.factories.RestTemplateFactory;
import com.qsocialnow.services.RetroactiveService;
import com.qsocialnow.services.ServiceUrlResolver;

@Service("retroactiveService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RetroactiveServiceImpl implements RetroactiveService {

    private static final Logger log = LoggerFactory.getLogger(RetroactiveServiceImpl.class);

    @Value("${retroactive.serviceurl}")
    private String retroactiveServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @Override
    public void executeNewProcess(RetroactiveProcessRequest request) {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            restTemplate.postForLocation(serviceUrlResolver.resolveUrl(retroactiveServiceUrl), request);
        } catch (Exception e) {
            log.error("There was an error while trying to call retroactive service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public RetroactiveProcess getCurrentProcess() {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            return restTemplate.getForObject(serviceUrlResolver.resolveUrl(retroactiveServiceUrl),
                    RetroactiveProcess.class);
        } catch (Exception e) {
            log.error("There was an error while trying to call retroactive service", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelProcess() {
        try {
            RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
            restTemplate.delete(serviceUrlResolver.resolveUrl(retroactiveServiceUrl));
        } catch (Exception e) {
            log.error("There was an error while trying to call retroactive service", e);
            throw new RuntimeException(e);
        }
    }

}
