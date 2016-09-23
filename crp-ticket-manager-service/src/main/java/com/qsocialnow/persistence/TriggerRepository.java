package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.request.TriggerListRequest;
import com.qsocialnow.common.pagination.PageRequest;
import com.qsocialnow.elasticsearch.services.config.TriggerService;

@Service
public class TriggerRepository {

    private Logger log = LoggerFactory.getLogger(TriggerRepository.class);

    @Autowired
    private TriggerService triggerElasticService;

    public Trigger save(String domainId, Trigger newTrigger) {
        try {
            String id = triggerElasticService.indexTrigger(domainId, newTrigger);
            newTrigger.setId(id);

            return newTrigger;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public List<TriggerListView> findAll(String domainId, PageRequest pageRequest, TriggerListRequest triggerListRequest) {
        List<TriggerListView> triggers = new ArrayList<>();

        try {
            List<Trigger> triggersRepo = triggerElasticService.getTriggers(domainId, pageRequest.getOffset(),
                    pageRequest.getLimit(), triggerListRequest);

            for (Trigger triggerRepo : triggersRepo) {
                TriggerListView triggerListView = new TriggerListView();
                triggerListView.setId(triggerRepo.getId());
                triggerListView.setName(triggerRepo.getName());
                triggerListView.setDescription(triggerRepo.getDescription());
                triggerListView.setStatus(triggerRepo.getStatus());
                triggerListView.setFromDate(triggerRepo.getInit());
                triggerListView.setToDate(triggerRepo.getEnd());
                triggerListView.setSegments(triggerRepo.getSegments());
                triggers.add(triggerListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return triggers;
    }

    public Trigger findOne(String triggerId) {
        Trigger trigger = null;

        try {
            trigger = triggerElasticService.findOne(triggerId);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return trigger;
    }

    public Trigger update(String domainId, Trigger trigger) {
        try {
            String id = triggerElasticService.updateTrigger(domainId, trigger);
            trigger.setId(id);
            return trigger;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

}
