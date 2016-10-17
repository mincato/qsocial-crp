package com.qsocialnow.eventresolver.mocks;

import java.util.ArrayList;
import java.util.List;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Filter;

public class MockDetectionCriteriaBuilder {

    public static List<DetectionCriteria> buildOneFalse() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("1");
        Filter filter = new Filter();
        filter.setId("false");
        detectionCriteria.setFilter(filter);
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

    public static List<DetectionCriteria> buildOneTrue() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("1");
        Filter filter = new Filter();
        filter.setId("true");
        detectionCriteria.setFilter(filter);
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

    public static List<DetectionCriteria> buildOneTrueInvalid() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("1");
        Filter filter = new Filter();
        filter.setId("true");
        detectionCriteria.setFilter(filter);
        detectionCriteria.setValidateTo(System.currentTimeMillis() - 10000);
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

    public static List<DetectionCriteria> buildTwoFalse() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("1");
        Filter filter = new Filter();
        filter.setId("false");
        detectionCriteria.setFilter(filter);
        detectionCriterias.add(detectionCriteria);
        detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("2");
        filter = new Filter();
        filter.setId("false");
        detectionCriteria.setFilter(filter);
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

    public static List<DetectionCriteria> buildTwoFalseTrue() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("1");
        Filter filter = new Filter();
        filter.setId("false");
        detectionCriteria.setFilter(filter);
        detectionCriterias.add(detectionCriteria);
        detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("2");
        filter = new Filter();
        filter.setId("true");
        detectionCriteria.setFilter(filter);
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

    public static List<DetectionCriteria> buildTwoTrueFalse() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("1");
        Filter filter = new Filter();
        filter.setId("true");
        detectionCriteria.setFilter(filter);
        detectionCriterias.add(detectionCriteria);
        detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("2");
        filter = new Filter();
        filter.setId("false");
        detectionCriteria.setFilter(filter);
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

    public static List<DetectionCriteria> buildThree() {
        List<DetectionCriteria> detectionCriterias = new ArrayList<>();
        DetectionCriteria detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("1");
        Filter filter = new Filter();
        filter.setId("false");
        detectionCriteria.setFilter(filter);
        detectionCriterias.add(detectionCriteria);
        detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("2");
        filter = new Filter();
        filter.setId("true");
        detectionCriteria.setFilter(filter);
        detectionCriterias.add(detectionCriteria);
        detectionCriteria = new DetectionCriteria();
        detectionCriteria.setId("3");
        filter = new Filter();
        filter.setId("false");
        detectionCriteria.setFilter(filter);
        detectionCriterias.add(detectionCriteria);
        return detectionCriterias;
    }

}
