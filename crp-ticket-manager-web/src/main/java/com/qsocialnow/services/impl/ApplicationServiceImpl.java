package com.qsocialnow.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.qsocialnow.model.ApplicationVersion;
import com.qsocialnow.services.ApplicationService;

@Service("applicationService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ApplicationServiceImpl implements ApplicationService {

    @Value("${Application-Version}")
    private String applicationVersion;

    @Value("${Application-Build}")
    private String applicationBuild;

    @Value("${Application-Branch}")
    private String applicationBranch;

    @Override
    public ApplicationVersion getVersion() {
        return new ApplicationVersion(applicationVersion, applicationBuild, applicationBranch);
    }

}
