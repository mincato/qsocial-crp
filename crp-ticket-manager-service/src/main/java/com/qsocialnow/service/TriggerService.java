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
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.persistence.TriggerRepository;

@Service
public class TriggerService {

    private static final Logger log = LoggerFactory.getLogger(TriggerService.class);

    @Autowired
    private FilterNormalizer filterNormalizer;

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private DomainService domainService;

    @Autowired
    private CuratorFramework zookeeperClient;

    @Value("${app.domains.path}")
    private String domainsPath;

    public Trigger createTrigger(String domainId, Trigger trigger) {
        Trigger triggerSaved = null;
        try {
            trigger.getSegments().stream().forEach(segment -> {
                segment.getDetectionCriterias().forEach(detectionCriteria -> {
                    filterNormalizer.normalizeFilter(detectionCriteria.getFilter());
                });
            });
            mockActions(trigger);
            mockResoultions(trigger, domainId);
            triggerSaved = triggerRepository.save(domainId, trigger);
            zookeeperClient.setData().forPath(domainsPath.concat(domainId));
        } catch (Exception e) {
            log.error("There was an error creating trigger: " + trigger.getName(), e);
            throw new RuntimeException(e.getMessage());
        }
        return triggerSaved;
    }

    public PageResponse<TriggerListView> findAll(String domainId, Integer pageNumber, Integer pageSize, String name) {
        List<TriggerListView> triggers = triggerRepository.findAll(domainId, new PageRequest(pageNumber, pageSize),
                name);

        PageResponse<TriggerListView> page = new PageResponse<TriggerListView>(triggers, pageNumber, pageSize);
        return page;
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

    private void mockResoultions(Trigger trigger, String domainId) {
        Domain domain = domainService.findOne(domainId);
        trigger.setResolutions(domain.getResolutions());
    }

}
