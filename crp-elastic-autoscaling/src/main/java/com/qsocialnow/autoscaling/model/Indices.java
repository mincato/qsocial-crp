package com.qsocialnow.autoscaling.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Indices {

    @SerializedName("docs")
    @Expose
    private Docs docs;

    @SerializedName("store")
    @Expose
    private Store store;

    @SerializedName("indexing")
    @Expose
    private Indexing indexing;

    public Indices(Docs docs, Store store, Indexing indexing) {
        this.docs = docs;
        this.store = store;
        this.indexing = indexing;
    }

    public Docs getDocs() {
        return docs;
    }

    public Store getStore() {
        return store;
    }

    public void setDocs(Docs docs) {
        this.docs = docs;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Indexing getIndexing() {
        return indexing;
    }

    public void setIndexing(Indexing indexing) {
        this.indexing = indexing;
    }
}
