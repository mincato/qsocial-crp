package com.qsocialnow.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.AutomaticActionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.common.pagination.PageResponse;
import com.qsocialnow.persistence.DomainRepository;

@Service
public class DomainService {

    private static final Logger log = LoggerFactory.getLogger(DomainService.class);

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private CuratorFramework zookeeperClient;

    @Value("${app.domains.path}")
    private String domainsPath;

    @Autowired
    private FilterNormalizer filterNormalizer;

    public Domain save(Domain newDomain) {
        Domain domainSaved = null;
        try {
            domainSaved = domainRepository.save(newDomain);
            zookeeperClient.create().forPath(domainsPath.concat(newDomain.getId()));
        } catch (Exception e) {
            log.error("There was an error saving domain: " + newDomain.getName(), e);
            // TODO rollback;
            throw new RuntimeException(e.getMessage());
        }
        return domainSaved;
    }

    public Domain findOne(String domainId) {
        Domain domain = domainRepository.findOne(domainId);
        return domain;
    }

    public Domain update(String domainId, Domain domain) {
        Domain domainSaved = null;
        try {
            domain.setId(domainId);
            domainSaved = domainRepository.save(domain);
        } catch (Exception e) {
            log.error("There was an error updating domain: " + domain.getName(), e);
            throw new RuntimeException(e.getMessage());
        }
        return domainSaved;
    }

    public PageResponse<DomainListView> findAll(Integer pageNumber, Integer pageSize) {
        List<DomainListView> domains = domainRepository.findAll(new PageRequest(pageNumber, pageSize));

        Long count = domainRepository.count();

        PageResponse<DomainListView> page = new PageResponse<DomainListView>(domains, pageNumber, pageSize, count);
        return page;
    }

    public PageResponse<DomainListView> findAllByName(Integer pageNumber, Integer pageSize, String name) {
        List<DomainListView> domains = domainRepository.findAllByName(new PageRequest(pageNumber, pageSize), name);

        Long count = domainRepository.count();

        PageResponse<DomainListView> page = new PageResponse<DomainListView>(domains, pageNumber, pageSize, count);
        return page;
    }

    public Domain createTrigger(String domainId, Trigger trigger) {
        Domain domainSaved = null;
        try {
            Domain domain = domainRepository.findOne(domainId);
            trigger.getSegments().stream().forEach(segment -> {
                segment.getDetectionCriterias().forEach(detectionCriteria -> {
                    filterNormalizer.normalizeFilter(detectionCriteria.getFilter());
                });
            });
            domain.addTrigger(trigger);
            mockActions(trigger);
            domainSaved = domainRepository.save(domain);
        } catch (Exception e) {
            log.error("There was an error creating trigger: " + trigger.getName(), e);
            throw new RuntimeException(e.getMessage());
        }
        return domainSaved;
    }

    private void mockActions(Trigger trigger) {
        trigger.getSegments().stream().forEach(segment -> {
            segment.getDetectionCriterias().stream().forEach(detectionCriteria -> {
                List<AutomaticActionCriteria> actions = new ArrayList<>();
                AutomaticActionCriteria automaticActionCriteria = new AutomaticActionCriteria();
                automaticActionCriteria.setActionType(ActionType.OPEN_CASE);
                actions.add(automaticActionCriteria);
                detectionCriteria.setAccionCriterias(actions);
            });
        });
    }

    public void setRepository(DomainRepository repository) {
        this.domainRepository = repository;
    }
}
