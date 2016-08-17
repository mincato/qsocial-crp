package com.qsocialnow.common.model.config;

public class SourceCredentials {

    private String id;

    private SourceIdentifier identifier;

    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SourceIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(SourceIdentifier identifier) {
        this.identifier = identifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
