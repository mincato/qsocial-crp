package com.qsocialnow.elasticsearch.repositories;

import java.util.List;

public class SearchResponse<T> {

    private String id;

    private String version;

    private T source;

    private List<T> sources;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    public List<T> getSources() {
        return this.sources;
    }

    public void setSources(List<T> sources) {
        this.sources = sources;
    }
}
