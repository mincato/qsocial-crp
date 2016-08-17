package com.qsocialnow.common.model.config;

public enum SourceIdentifier {

    TWITTER("Twitter", "TWITTER"), FACEBOOK("Facebook", "FACEBOOK");

    String name;

    String id;

    SourceIdentifier(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
