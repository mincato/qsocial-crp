package com.qsocialnow.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.DomainListView;
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.persistence.DomainRepository;
import com.qsocialnow.persistence.SegmentRepository;
import com.qsocialnow.persistence.TeamRepository;
import com.qsocialnow.persistence.TriggerRepository;

@Service
public class DomainService {

    private static final Logger log = LoggerFactory.getLogger(DomainService.class);

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private CuratorFramework zookeeperClient;

    @Value("${app.domains.path}")
    private String domainsPath;

    public Domain save(Domain newDomain) {
        Domain domainSaved = null;
        try {
            domainSaved = domainRepository.save(newDomain);
            if (domainSaved.isActive()) {
                zookeeperClient.create().forPath(domainsPath.concat(newDomain.getId()));
            }
        } catch (Exception e) {
            log.error("There was an error saving domain: " + newDomain.getName(), e);
            throw new RuntimeException(e);
        }
        return domainSaved;
    }

    public Domain findOne(String domainId) {
        Domain domain = domainRepository.findOne(domainId);
        return domain;
    }

    public Domain findOneWithActiveResolutions(String domainId) {
        Domain domain = domainRepository.findOneWithActiveResolutions(domainId);
        return domain;
    }

    public Domain update(String domainId, Domain domain) {
        Domain domainSaved = null;
        try {
            domain.setId(domainId);
            domainSaved = domainRepository.update(domain);
            updateZookeeperDomainNode(domainId, domainSaved.isActive());
        } catch (Exception e) {
            log.error("There was an error updating domain: " + domain.getName(), e);
            throw new RuntimeException(e);
        }
        return domainSaved;
    }

    private void updateZookeeperDomainNode(String domainId, boolean active) throws Exception {
        String path = domainsPath.concat(domainId);
        Stat stat = zookeeperClient.checkExists().forPath(path);
        if (stat == null) {
            if (active) {
                zookeeperClient.create().forPath(path);
            }
        } else {
            if (active) {
                zookeeperClient.setData().forPath(path);
            } else {
                zookeeperClient.delete().forPath(path);
            }
        }
    }

    public PageResponse<DomainListView> findAll(Integer pageNumber, Integer pageSize) {
        List<DomainListView> domains;
        if (pageNumber == null && pageSize == null) {
            domains = domainRepository.findAll();
        } else {
            domains = domainRepository.findAll(new PageRequest(pageNumber, pageSize, null));
        }
        PageResponse<DomainListView> page = new PageResponse<DomainListView>(domains, pageNumber, pageSize);
        return page;
    }

    public List<DomainListView> findAllActive() {
        List<DomainListView> domains = domainRepository.findAllActive();
        return domains;
    }

    public PageResponse<DomainListView> findAllByUserName(String userName) {
        log.info("Retrieving domains from userName:" + userName);
        List<Team> teams = teamRepository.findTeams(userName);
        List<DomainListView> domains = null;

        if (teams != null && !teams.isEmpty()) {
            List<Segment> segments = segmentRepository.findSegmentsByTeams(teams);
            if (segments != null && !segments.isEmpty()) {
                List<String> triggerIds = new ArrayList<>();
                for (Segment segment : segments) {
                    triggerIds.add(segment.getTriggerId());
                }
                List<Trigger> triggers = triggerRepository.findTriggersByIds(triggerIds);
                if (triggers != null && !triggers.isEmpty()) {
                    List<String> domainsIds = new ArrayList<>();
                    for (Trigger trigger : triggers) {
                        domainsIds.add(trigger.getDomainId());
                    }
                    domains = domainRepository.findDomainsByIds(domainsIds);
                }
            }
        }
        PageResponse<DomainListView> page = new PageResponse<DomainListView>(domains, 0, -1);
        return page;
    }

    public PageResponse<DomainListView> findAllByName(Integer pageNumber, Integer pageSize, String name) {
        List<DomainListView> domains = domainRepository
                .findAllByName(new PageRequest(pageNumber, pageSize, null), name);

        PageResponse<DomainListView> page = new PageResponse<DomainListView>(domains, pageNumber, pageSize);
        return page;
    }

    public void setRepository(DomainRepository repository) {
        this.domainRepository = repository;
    }
}
