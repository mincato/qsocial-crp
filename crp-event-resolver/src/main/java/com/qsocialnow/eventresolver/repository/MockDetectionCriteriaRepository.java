package com.qsocialnow.eventresolver.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.FilterType;

public class MockDetectionCriteriaRepository implements DetectionCriteriaRepository {

    @Override
    public List<DetectionCriteria> findByDomain(String domain) {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        Filter filter = new Filter();
        filter.setType(FilterType.FALSE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        detectionCriteria = new DetectionCriteria();
        filter = new Filter();
        filter.setType(FilterType.TRUE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        detectionCriteria = new DetectionCriteria();
        filter = new Filter();
        filter.setType(FilterType.FALSE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

}
