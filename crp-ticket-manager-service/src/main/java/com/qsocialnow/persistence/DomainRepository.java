package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.gson.GsonBuilder;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.services.config.DomainService;

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

    public Domain update(Domain domain) {
        try {
            byte[] configuratorBytes = zookeeperClient.getData().forPath(elasticConfiguratorZnodePath);
            Configurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                    Configurator.class);

            String id = domainElasticService.updateDomain(configurator, domain);
            domain.setId(id);
            return domain;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public Domain findOne(String domainId) {
        try {
            byte[] configuratorBytes = zookeeperClient.getData().forPath(elasticConfiguratorZnodePath);
            Configurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                    Configurator.class);

            Domain domain = domainElasticService.findDomainWithResolutions(configurator, domainId);

            return domain;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public List<DomainListView> findAll(PageRequest pageRequest) {
        List<DomainListView> domains = new ArrayList<>();

        try {
            byte[] configuratorBytes = zookeeperClient.getData().forPath(elasticConfiguratorZnodePath);
            Configurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                    Configurator.class);

            List<Domain> domainsRepo = domainElasticService.getDomains(configurator, pageRequest.getOffset(),
                    pageRequest.getLimit());

            for (Domain domainRepo : domainsRepo) {
                DomainListView domainListView = new DomainListView();
                domainListView.setId(domainRepo.getId());
                domainListView.setName(domainRepo.getName());

                List<Long> thematics = domainRepo.getThematics();
                if (thematics != null) {
                    String values = thematics.stream().map(number -> String.valueOf(number))
                            .collect(Collectors.joining(", "));

                    domainListView.setThematics(values);
                }
                domains.add(domainListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return domains;
    }

    public List<DomainListView> findAllByName(PageRequest pageRequest, String name) {
        List<DomainListView> domains = new ArrayList<>();

        try {
            byte[] configuratorBytes = zookeeperClient.getData().forPath(elasticConfiguratorZnodePath);
            Configurator configurator = new GsonBuilder().create().fromJson(new String(configuratorBytes),
                    Configurator.class);

            List<Domain> domainsRepo = domainElasticService.getDomainsByName(configurator, pageRequest.getOffset(),
                    pageRequest.getLimit(), name);

            for (Domain domainRepo : domainsRepo) {
                DomainListView domainListView = new DomainListView();
                domainListView.setId(domainRepo.getId());
                domainListView.setName(domainRepo.getName());

                List<Long> thematics = domainRepo.getThematics();
                if (thematics != null) {
                    String values = thematics.stream().map(number -> String.valueOf(number))
                            .collect(Collectors.joining(", "));

                    domainListView.setThematics(values);
                }
                domains.add(domainListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return domains;
    }

    public Long count() {
        return 50L;
    }
}
