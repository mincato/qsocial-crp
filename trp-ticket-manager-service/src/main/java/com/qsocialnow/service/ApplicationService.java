package com.qsocialnow.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qsocialnow.model.ApplicationVersion;

@Service
public class ApplicationService {

    @Value("${Implementation-Version}")
    private String implementationVersion;

    public ApplicationVersion getVersion() {
        return new ApplicationVersion(implementationVersion);
    }

}
