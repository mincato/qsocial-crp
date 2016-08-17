package com.qsocialnow.persistence;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.services.DomainService;

@Service
public class DomainRepository {

    private Logger log = LoggerFactory.getLogger(DomainRepository.class);

    @Autowired
    private DomainService domainElasticService;

    @Value("${app.elastic.config.configurator.path}")
    private String elasticConfiguratorZnodePath;

    @Autowired
    @Qualifier("zookeeperClient")
    private CuratorFramework zookeeperClient;

    public Domain save(Domain newDomain) {
        try {
            byte[] configuratorBytes = zookeeperClient.getData().forPath(elasticConfiguratorZnodePath);
            Configurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                    Configurator.class);

            String id = domainElasticService.indexDomain(configurator, newDomain);
            newDomain.setId(id);

            return newDomain;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

}
