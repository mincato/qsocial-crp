package com.qsocialnow.model;

public class ApplicationVersion {

    private final String version;

    private final String build;

    public ApplicationVersion(String version, String build) {
        this.version = version;
        this.build = build;
    }

    public String getVersion() {
        return version;
    }

    public String getBuild() {
        return build;
    }
}
