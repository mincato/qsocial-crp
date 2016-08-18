package com.qsocialnow.eventresolver.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.FilterType;
import com.qsocialnow.eventresolver.filters.DetectionCriteriaFilter;
import com.qsocialnow.eventresolver.filters.FalseDetectionCriteriaFilter;
import com.qsocialnow.eventresolver.filters.TrueDetectionCriteriaFilter;
import com.qsocialnow.eventresolver.model.event.InPutBeanDocument;

@Service
public class DetectionCriteriaResolver {

    private final Map<FilterType, DetectionCriteriaFilter> filters;

    private static final Logger log = LoggerFactory.getLogger(DetectionCriteriaResolver.class);

    public DetectionCriteriaResolver() {
        filters = new HashMap<>();
        filters.put(FilterType.FALSE, new FalseDetectionCriteriaFilter());
        filters.put(FilterType.TRUE, new TrueDetectionCriteriaFilter());
    }

    public DetectionCriteria resolve(InPutBeanDocument message, List<DetectionCriteria> detectionCriterias) {
        DetectionCriteria detectionCriteria = null;
        if (detectionCriterias != null) {
            boolean match = false;
            DetectionCriteria currentDetectionCriteria = null;
            for (int i = 0; !match && i < detectionCriterias.size(); i++) {
                currentDetectionCriteria = detectionCriterias.get(i);
                DetectionCriteriaFilter messageFilter = filters.get(currentDetectionCriteria.getFilter().getType());
                if (messageFilter != null) {
                    log.info(String.format("Executing filter: %s", currentDetectionCriteria.getFilter().getType()));
                    match = messageFilter.match(message, currentDetectionCriteria.getFilter().getParameters());
                } else {
                    log.warn(String.format("There is no filter implementation for: %s", currentDetectionCriteria
                            .getFilter().getType()));
                }
            }
            if (match) {
                detectionCriteria = currentDetectionCriteria;
            }
        }
        return detectionCriteria;
    }

}
