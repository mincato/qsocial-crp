package com.qsocialnow.elasticsearch.repositories;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;

import com.qsocialnow.elasticsearch.mappings.ChildMapping;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.IdentityType;

public interface Repository<T> {

    public void initClient();

    public void closeClient();

    public void createIndex(String index);

    public boolean validateIndex(String index);
    
    public boolean updateIndexAlias(String index,String alias);

    public <E> String indexMapping(Mapping<T, E> mapping, T document);

    public <E> IndexResponse<E> bulkOperation(Mapping<T, E> mapping, List<IdentityType> documents);

    public <E> void removeChildMapping(String id, ChildMapping<T, E> mapping);

    public <E> String indexChildMapping(ChildMapping<T, E> mapping, T document);

    public <E> String updateChildMapping(String id, ChildMapping<T, E> mapping, T document);

    public <E> String updateIndexMapping(String id, Mapping<T, E> mapping, T document);

    public <E> SearchResponse<E> find(String id, Mapping<T, E> mapping);
    
    public <E> SearchResponse<E> queryByField(Mapping<T, E> mapping, int from, int size, String sortField,
            String serchField, String searchValue);

    public <E> SearchResponse<E> queryMatchAll(int from, int size, String sortField, Mapping<T, E> mapping);

        public <E> SearchResponse<E> searchChildMapping(int from, int size, String sortField, ChildMapping<T, E> mapping);

    public <E> SearchResponse<E> searchChildMapping(ChildMapping<T, E> mapping);

    public <E> SearchResponse<E> searchChildMappingWithFilters(int from, int size, String sortField,
            QueryBuilder filters, ChildMapping<T, E> mapping);

}
