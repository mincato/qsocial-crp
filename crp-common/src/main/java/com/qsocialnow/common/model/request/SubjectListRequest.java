package com.qsocialnow.common.model.request;

public class SubjectListRequest {

    private final String identifier;

    private final String source;

    private final String sourceName;

    private final String keyword;

    public SubjectListRequest(String keyword, String identifier, String source, String sourceName) {
        this.keyword = keyword;
        this.identifier = identifier;
        this.source = source;
        this.sourceName = sourceName;
    }

    public String getKeyword() {
        return keyword;
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
