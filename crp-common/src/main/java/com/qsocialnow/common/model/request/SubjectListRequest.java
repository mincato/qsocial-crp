package com.qsocialnow.common.model.request;

public class SubjectListRequest {

    private final String identifier;

    private final String source;

    private final String sourceName;

    public SubjectListRequest(String identifier, String source, String sourceName) {
        this.identifier = identifier;
        this.source = source;
        this.sourceName = sourceName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getSource() {
        return source;
    }

    public String getSourceName() {
        return sourceName;
    }

}
