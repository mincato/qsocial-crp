package com.qsocialnow.services;

import com.qsocialnow.common.model.config.Resolution;

public interface ResolutionService {

    Resolution create(String domainId, Resolution resolution);

}
