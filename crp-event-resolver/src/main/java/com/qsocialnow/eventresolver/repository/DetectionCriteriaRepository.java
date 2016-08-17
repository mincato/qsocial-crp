package com.qsocialnow.eventresolver.repository;

import java.util.List;

import com.qsocialnow.common.model.config.DetectionCriteria;

public interface DetectionCriteriaRepository {

    List<DetectionCriteria> findByDomain(String domain);

}
