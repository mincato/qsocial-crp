package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.SegmentListView;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.config.TriggerListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.request.TriggerListRequest;
import com.qsocialnow.elasticsearch.services.config.SegmentService;
import com.qsocialnow.elasticsearch.services.config.TriggerService;

@Service
public class TriggerRepository implements ReportRepository {

    private Logger log = LoggerFactory.getLogger(TriggerRepository.class);

    @Autowired
    private TriggerService triggerElasticService;

    @Autowired
    private SegmentService segmentElasticService;

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
            copyTriggerRepoToTriggerListView(triggers, triggersRepo);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return triggers;
    }

    public List<TriggerListView> findAllActive(String domainId) {
        List<TriggerListView> triggers = new ArrayList<>();

        try {
            List<Trigger> triggersRepo = triggerElasticService.getActiveTriggers(domainId);
            copyTriggerRepoToTriggerListView(triggers, triggersRepo);
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

    public Trigger findWithSegments(String triggerId) {
        Trigger trigger = null;

        try {
            trigger = triggerElasticService.findTriggerWithSegments(triggerId);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return trigger;
    }

    public Trigger findWithActiveSegments(String triggerId) {
        Trigger trigger = null;

        try {
            trigger = triggerElasticService.findTriggerWithActiveSegments(triggerId);
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

    public Segment findSegment(String segmentId) {
        Segment segment = null;

        try {
            segment = segmentElasticService.getSegment(segmentId);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return segment;
    }

    public List<SegmentListView> findSegments(String triggerId) {
        List<SegmentListView> segments = new ArrayList<>();
        List<Segment> segmentsRepo = segmentElasticService.getSegments(triggerId);

        copySegmentRepoToSegmentListView(segments, segmentsRepo);
        return segments;
    }

    public List<SegmentListView> findActiveSegments(String triggerId) {
        List<SegmentListView> segments = new ArrayList<>();
        List<Segment> segmentsRepo = segmentElasticService.getActiveSegments(triggerId);

        copySegmentRepoToSegmentListView(segments, segmentsRepo);
        return segments;
    }

    public List<Trigger> findTriggersByIds(List<String> triggerIds) {
        List<Trigger> triggers = triggerElasticService.getTriggersByIds(triggerIds);
        return triggers;
    }

    public Map<String, String> findAllReport() {
        return triggerElasticService.getAllTriggersAsMap();
    }

    private void copyTriggerRepoToTriggerListView(List<TriggerListView> triggers, List<Trigger> triggersRepo) {
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
    }

    private void copySegmentRepoToSegmentListView(List<SegmentListView> segments, List<Segment> segmentsRepo) {
        for (Segment segmentRepo : segmentsRepo) {
            SegmentListView segmentListView = new SegmentListView();
            segmentListView.setId(segmentRepo.getId());
            segmentListView.setDescription(segmentRepo.getDescription());
            segmentListView.setTeamId(segmentRepo.getTeam());
            segmentListView.setActive(segmentRepo.isActive());
            segments.add(segmentListView);
        }
    }
}
