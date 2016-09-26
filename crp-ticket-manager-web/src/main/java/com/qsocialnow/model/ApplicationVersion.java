package com.qsocialnow.model;

public class ApplicationVersion {

    private final String version;

    private final String build;

    private final String branch;

    public ApplicationVersion(String version, String build, String branch) {
        this.version = version;
        this.build = build;
        this.branch = branch;
    }

    public String getVersion() {
        return version;
    }

    public String getBuild() {
        return build;
    }

    public String getBranch() {
        return branch;
    }
}
