package com.qsocialnow.eventresolver.processor;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.eventresolver.filters.DetectionCriteriaFilter;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Service
public class DetectionCriteriaResolver {

    @Resource
    private List<DetectionCriteriaFilter> filters;

    private static final Logger log = LoggerFactory.getLogger(DetectionCriteriaResolver.class);

    public DetectionCriteria resolve(NormalizedInputBeanDocument normalizedMessage,
            List<DetectionCriteria> detectionCriterias) {
        log.info("resolving detection criteria...");
        DetectionCriteria detectionCriteria = null;
        if (detectionCriterias != null) {
            boolean match = false;
            DetectionCriteria currentDetectionCriteria = null;
            for (int i = 0; !match && i < detectionCriterias.size(); i++) {
                currentDetectionCriteria = detectionCriterias.get(i);
                Filter configFilter = currentDetectionCriteria.getFilter();
                match = filters.stream().allMatch(filter -> {
                    if (filter.apply(configFilter)) {
                        return filter.match(normalizedMessage, configFilter);
                    } else {
                        return true;
                    }
                });

            }
            if (match) {
                detectionCriteria = currentDetectionCriteria;
            }
        }
        return detectionCriteria;
    }

    public void setFilters(List<DetectionCriteriaFilter> filters) {
        this.filters = filters;
    }
}
