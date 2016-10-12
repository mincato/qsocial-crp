package com.qsocialnow.common.model.request;

public class SubjectListRequest {

    private final String identifier;

    private final String source;

    public SubjectListRequest(String identifier, String source) {
        this.identifier = identifier;
        this.source = source;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getSource() {
        return source;
    }

}
