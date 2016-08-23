package com.qsocialnow.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.services.ServiceUrlResolver;
import com.qsocialnow.services.TriggerService;

@Service("triggerService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TriggerServiceImpl implements TriggerService {

    private static final Logger log = LoggerFactory.getLogger(TriggerServiceImpl.class);

    @Value("${trigger.serviceurl}")
    private String triggerServiceUrl;

    @Autowired
    private ServiceUrlResolver serviceUrlResolver;

    @Override
    public Trigger create(Trigger trigger) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Trigger createdTrigger = restTemplate.postForObject(
                    serviceUrlResolver.resolveUrl("centaurico", triggerServiceUrl), trigger, Trigger.class);
            return createdTrigger;
        } catch (Exception e) {
            log.error("There was an error while trying to call trigger service", e);
            throw new RuntimeException(e);
        }
    }

}
