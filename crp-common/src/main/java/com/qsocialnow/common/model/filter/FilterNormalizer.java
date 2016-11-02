package com.qsocialnow.common.model.filter;

import java.util.Arrays;
import java.util.HashSet;

import com.qsocialnow.common.model.config.AdmUnitFilter;
import com.qsocialnow.common.model.config.AdminUnit;
import com.qsocialnow.common.model.config.BaseAdminUnit;
import com.qsocialnow.common.model.config.Filter;
import com.qsocialnow.common.util.FilterConstants;
import com.qsocialnow.common.util.StringUtils;

public class FilterNormalizer {

    public void normalizeFilter(Filter filter) {
        if (filter != null && filter.getWordFilters() != null) {
            filter.getWordFilters()
                    .stream()
                    .forEach(
                            wordFilter -> {
                                if (wordFilter.getInputText() != null) {
                                    switch (wordFilter.getType()) {
                                        case HASHTAG:
                                        case MENTION:
                                            wordFilter.setText(StringUtils.convert2LowerCase(wordFilter.getInputText()
                                                    .split(FilterConstants.COMMA_SEPARATOR)));
                                            break;
                                        case TEXT:
                                            wordFilter.setText(StringUtils.convertLowerCaseAsciiFolding(wordFilter
                                                    .getInputText().split(FilterConstants.COMMA_SEPARATOR)));
                                            break;
                                        default:
                                            wordFilter.setText(new HashSet<>(Arrays.asList(wordFilter.getInputText()
                                                    .split(FilterConstants.COMMA_SEPARATOR))));
                                            break;
                                    }
                                }
                            });
        }
        if (filter != null && filter.getAdmUnitFilter() != null) {
            filter.getAdmUnitFilter().stream().forEach(admUnitFilter -> {
                normalizeAdmUnitFilter(admUnitFilter);
            });
        }
    }

    public void normalizeAdmUnitFilter(AdmUnitFilter admUnitFilter) {
        AdminUnit adminUnit = admUnitFilter.getAdminUnit();
        fillAdmUnitFilter(admUnitFilter, adminUnit);
        for (BaseAdminUnit baseAdminUnit : adminUnit.getParents()) {
            fillAdmUnitFilter(admUnitFilter, baseAdminUnit);
        }
    }

    private void fillAdmUnitFilter(AdmUnitFilter admUnitFilter, BaseAdminUnit adminUnit) {
        switch (adminUnit.getType()) {
            case CONTINENT:
                admUnitFilter.setContinent(adminUnit.getGeoNameId());
                break;
            case COUNTRY:
                admUnitFilter.setCountry(adminUnit.getGeoNameId());
                break;
            case CITY:
                admUnitFilter.setCity(adminUnit.getGeoNameId());
                break;
            case NEIGHBORHOOD:
                admUnitFilter.setNeighborhood(adminUnit.getGeoNameId());
                break;
            case ADM1:
                admUnitFilter.setAdm1(adminUnit.getGeoNameId());
                break;
            case ADM2:
                admUnitFilter.setAdm2(adminUnit.getGeoNameId());
                break;
            case ADM3:
                admUnitFilter.setAdm3(adminUnit.getGeoNameId());
                break;
            case ADM4:
                admUnitFilter.setAdm4(adminUnit.getGeoNameId());
                break;
            default:
                break;
        }
    }

}
