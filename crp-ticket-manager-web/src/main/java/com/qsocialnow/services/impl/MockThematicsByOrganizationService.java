package com.qsocialnow.services.impl;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.qsocialnow.model.OrganizationId;
import com.qsocialnow.model.ThematicsByClientOrganizationIdOutput;
import com.qsocialnow.services.ThematicsByOrganizationService;

@Service("mockThematicsByOrganizationService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MockThematicsByOrganizationService implements ThematicsByOrganizationService {

    @Override
    public ThematicsByClientOrganizationIdOutput thematicsByClientOrganizationId(OrganizationId organizationId) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        InputStream systemResourceAsStream = getClass().getResourceAsStream("/mocks/thematics.json");
        return gsonBuilder.create().fromJson(new InputStreamReader(systemResourceAsStream),
                ThematicsByClientOrganizationIdOutput.class);
    }
}
