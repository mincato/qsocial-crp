package com.qsocialnow.elasticsearch.repositories;

import java.util.List;

import io.searchbox.core.BulkResult.BulkResultItem;

public class IndexResponse<T> {

    private String id;

    private String version;

    private T source;

    private List<T> sources;

    private List<BulkResultItem> sourcesBulk;

    private List<BulkResultItem> failedItems;

    private boolean isSucceeded;

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

    public List<BulkResultItem> getSourcesBulk() {
        return sourcesBulk;
    }

    public void setSourcesBulk(List<BulkResultItem> sourcesBulk) {
        this.sourcesBulk = sourcesBulk;
    }

    public List<BulkResultItem> getFailedItems() {
        return failedItems;
    }

    public void setFailedItems(List<BulkResultItem> failedItems) {
        this.failedItems = failedItems;
    }

    public boolean isSucceeded() {
        return isSucceeded;
    }

    public void setSucceeded(boolean isSucceeded) {
        this.isSucceeded = isSucceeded;
    }
}
