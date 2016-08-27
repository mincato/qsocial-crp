package com.qsocialnow.eventresolver.processor;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.FilterType;
import com.qsocialnow.eventresolver.filters.DetectionCriteriaFilter;
import com.qsocialnow.eventresolver.model.event.InPutBeanDocument;

@Service
public class DetectionCriteriaResolver {

    @Resource
    private Map<FilterType, DetectionCriteriaFilter> filters;

    private static final Logger log = LoggerFactory.getLogger(DetectionCriteriaResolver.class);

    public DetectionCriteria resolve(InPutBeanDocument message, List<DetectionCriteria> detectionCriterias) {
        DetectionCriteria detectionCriteria = null;
        if (detectionCriterias != null) {
            boolean match = false;
            DetectionCriteria currentDetectionCriteria = null;
            for (int i = 0; !match && i < detectionCriterias.size(); i++) {
                currentDetectionCriteria = detectionCriterias.get(i);
                match = currentDetectionCriteria.getFilters().stream().allMatch(filter -> {
                    DetectionCriteriaFilter messageFilter = filters.get(filter.getType());
                    if (messageFilter != null) {
                        log.info(String.format("Executing filter: %s", filter.getType()));
                        return messageFilter.match(message, filter.getParameters());
                    } else {
                        log.warn(String.format("There is no filter implementation for: %s", filter.getType()));
                        return false;
                    }
                });

            }
            if (match) {
                detectionCriteria = currentDetectionCriteria;
            }
        }
        return detectionCriteria;
    }

    public void setFilters(Map<FilterType, DetectionCriteriaFilter> filters) {
        this.filters = filters;
    }

}
