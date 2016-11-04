package com.qsocialnow.common.model.cases;

import java.io.Serializable;

import com.qsocialnow.common.model.config.Media;

public class SubjectListView implements Serializable {

    private static final long serialVersionUID = 8113061120707981442L;

    private String id;

    private String identifier;

    private Long source;

    private String sourceName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Media getMedia() {
        return Media.getByValue(this.source);
    }
}
