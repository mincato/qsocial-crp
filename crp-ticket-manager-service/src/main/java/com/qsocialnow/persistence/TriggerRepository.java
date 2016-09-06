package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.services.config.TriggerService;

@Service
public class TriggerRepository {

    private Logger log = LoggerFactory.getLogger(TriggerRepository.class);

    @Autowired
    private TriggerService triggerElasticService;

    @Autowired
    private Configurator elasticConfig;

    public Trigger save(String domainId, Trigger newTrigger) {
        try {
            String id = triggerElasticService.indexTrigger(elasticConfig, domainId, newTrigger);
            newTrigger.setId(id);

            return newTrigger;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public List<TriggerListView> findAll(String domainId, PageRequest pageRequest) {
        List<TriggerListView> triggers = new ArrayList<>();

        try {
            List<Trigger> triggersRepo = triggerElasticService.getTriggers(elasticConfig, domainId,
                    pageRequest.getOffset(), pageRequest.getLimit());

            for (Trigger triggerRepo : triggersRepo) {
                TriggerListView triggerListView = new TriggerListView();
                triggerListView.setId(triggerRepo.getId());
                triggerListView.setName(triggerRepo.getName());
                triggerListView.setDescription(triggerRepo.getDescription());
                triggerListView.setStatus(triggerRepo.getStatus());

                triggers.add(triggerListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return triggers;
    }

}
