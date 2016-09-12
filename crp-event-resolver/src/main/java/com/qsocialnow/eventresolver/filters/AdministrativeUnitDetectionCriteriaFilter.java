package com.qsocialnow.eventresolver.filters;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.common.model.config.AdmUnitFilter;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.eventresolver.normalizer.NormalizedInputBeanDocument;

@Component("administrativeUnitDetectionCriteriaFilter")
public class AdministrativeUnitDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(AdministrativeUnitDetectionCriteriaFilter.class);

    @Override
    public boolean match(NormalizedInputBeanDocument message, Filter filter) {
        log.info("Executing administrative unit message filter");
        boolean match = false;
        if (messageHasAdmUnit(message)) {
            match = filter.getAdmUnitFilter().stream()
                    .anyMatch(admUnitFilter -> equalsAdmUnits(message, admUnitFilter));
        }
        return match;
    }

    private boolean equalsAdmUnits(NormalizedInputBeanDocument message, AdmUnitFilter admUnitFilter) {
        return equalsAttribute(message.getContinent(), admUnitFilter.getContinent())
                && equalsAttribute(message.getCountry(), admUnitFilter.getCountry())
                && equalsAttribute(message.getAdm1(), admUnitFilter.getAdm1())
                && equalsAttribute(message.getAdm2(), admUnitFilter.getAdm2())
                && equalsAttribute(message.getAdm3(), admUnitFilter.getAdm3())
                && equalsAttribute(message.getAdm4(), admUnitFilter.getAdm4())
                && equalsAttribute(message.getCity(), admUnitFilter.getCity())
                && equalsAttribute(message.getNeighborhood(), admUnitFilter.getNeighborhood());
    }

    private boolean equalsAttribute(Long filterAttribute, Long messageAttribute) {
        boolean areEquals;
        if (filterAttribute != null) {
            areEquals = filterAttribute.equals(messageAttribute);
        } else {
            areEquals = messageAttribute == null;
        }
        return areEquals;
    }

    private boolean messageHasAdmUnit(NormalizedInputBeanDocument message) {
        return message.getContinent() != null || message.getCountry() != null || message.getAdm1() != null
                || message.getAdm2() != null || message.getAdm3() != null || message.getAdm4() != null
                || message.getCity() != null || message.getNeighborhood() != null;
    }

    @Override
    public boolean apply(Filter filter) {
        return CollectionUtils.isNotEmpty(filter.getAdmUnitFilter());
    }

}
