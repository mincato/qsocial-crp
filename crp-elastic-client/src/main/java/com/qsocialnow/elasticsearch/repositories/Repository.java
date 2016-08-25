package com.qsocialnow.elasticsearch.repositories;

import java.util.List;

import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.ChildType;

public interface Repository<T> {

    public void initClient();

    public void closeClient();

    public void createIndex(String index);

    public boolean validateIndex(String index);

    public <E> String indexMapping(Mapping<T, E> mapping, T document);

    public <E> IndexResponse<E> bulkOperation(Mapping<T, E> mapping, List<T> documents);

	public <E> String indexChildMapping(Mapping<T, E> mapping, ChildType document);    
	
	public <E> String updateIndexMapping(String id, Mapping<T, E> mapping, T document);

    public <E> SearchResponse<E> find(String id, Mapping<T, E> mapping);

    public <E> SearchResponse<E> query(Mapping<T, E> mapping, String searchValue);

    public <E> SearchResponse<E> search(int from, int size, String sortField, Mapping<T, E> mapping);
}
