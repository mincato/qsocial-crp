package com.qsocialnow.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.qsocialnow.model.Thematic;
import com.qsocialnow.model.ThematicsByClientOrganizationIdOutput;
import com.qsocialnow.services.ThematicService;
import com.qsocialnow.services.ThematicsByOrganizationService;

@Service("thematicService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ThematicServiceImpl implements ThematicService {

    @Autowired
    private ThematicsByOrganizationService thematicsByOrganizationService;

    @Override
    public List<Thematic> findAll() {
        ThematicsByClientOrganizationIdOutput thematicsByClientOrganizationId = thematicsByOrganizationService
                .thematicsByClientOrganizationId(null);
        return thematicsByClientOrganizationId.getThematics();
    }

}
