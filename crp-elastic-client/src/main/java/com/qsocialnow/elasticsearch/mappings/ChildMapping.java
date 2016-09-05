package com.qsocialnow.elasticsearch.mappings;

public interface ChildMapping<T, E> extends Mapping<T, E> {

    public String getIdParent();

    public void setIdParent(String idParent);

}
