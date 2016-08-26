package com.qsocialnow.eventresolver.mocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.model.config.FilterType;

public class MockDetectionCriteriaBuilder {

    public static List<DetectionCriteria> buildOneFalse() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("1");
        Filter filter = new Filter();
        filter.setType(FilterType.FALSE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

    public static List<DetectionCriteria> buildOneTrue() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("1");
        Filter filter = new Filter();
        filter.setType(FilterType.TRUE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

    public static List<DetectionCriteria> buildTwoFalse() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("1");
        Filter filter = new Filter();
        filter.setType(FilterType.FALSE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("2");
        filter = new Filter();
        filter.setType(FilterType.FALSE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

    public static List<DetectionCriteria> buildTwoFalseTrue() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("1");
        Filter filter = new Filter();
        filter.setType(FilterType.FALSE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("2");
        filter = new Filter();
        filter.setType(FilterType.TRUE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

    public static List<DetectionCriteria> buildTwoTrueFalse() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("1");
        Filter filter = new Filter();
        filter.setType(FilterType.TRUE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("2");
        filter = new Filter();
        filter.setType(FilterType.FALSE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

    public static List<DetectionCriteria> buildThree() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("1");
        Filter filter = new Filter();
        filter.setType(FilterType.FALSE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("2");
        filter = new Filter();
        filter.setType(FilterType.TRUE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("3");
        filter = new Filter();
        filter.setType(FilterType.FALSE);
        detectionCriteria.setFilters(Arrays.asList(filter));
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

}
