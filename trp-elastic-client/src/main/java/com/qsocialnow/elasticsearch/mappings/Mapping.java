package com.qsocialnow.elasticsearch.mappings;

public interface Mapping<T, E> {

    public String getIndex();

    public String getType();

    public String getMappingDefinition();

    public Class<?> getClassType();

    public T getDocumentType(E document);

}
