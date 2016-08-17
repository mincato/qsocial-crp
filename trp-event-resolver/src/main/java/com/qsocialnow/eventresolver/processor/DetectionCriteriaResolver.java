package com.qsocialnow.eventresolver.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.qsocial.eventresolver.filters.FalseMessageFilter;
import com.qsocial.eventresolver.filters.MessageFilter;
import com.qsocial.eventresolver.filters.TrueMessageFilter;
import com.qsocial.eventresolver.model.event.InPutBeanDocument;
import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.FilterType;

@Service
public class DetectionCriteriaResolver {

    private final Map<FilterType, MessageFilter> filters;

    private static final Logger log = LoggerFactory.getLogger(DetectionCriteriaResolver.class);

    public DetectionCriteriaResolver() {
        filters = new HashMap<>();
        filters.put(FilterType.FALSE, new FalseMessageFilter());
        filters.put(FilterType.TRUE, new TrueMessageFilter());
    }

    public DetectionCriteria resolve(InPutBeanDocument message, List<DetectionCriteria> detectionCriterias) {
        DetectionCriteria detectionCriteria = null;
        if (detectionCriterias != null) {
            boolean match = false;
            DetectionCriteria currentDetectionCriteria = null;
            for (int i = 0; !match && i < detectionCriterias.size(); i++) {
                currentDetectionCriteria = detectionCriterias.get(i);
                MessageFilter messageFilter = filters.get(currentDetectionCriteria.getFilter().getType());
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
