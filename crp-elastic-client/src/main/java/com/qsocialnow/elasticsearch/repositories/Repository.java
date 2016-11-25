package com.qsocialnow.elasticsearch.repositories;

import io.searchbox.core.SearchResult;

import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.qsocialnow.elasticsearch.mappings.ChildMapping;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.IdentityType;

public interface Repository<T> {

    public void initClient();

    public void closeClient();

    public void createIndex(String index, String jsonSettings);

    public void createMapping(String index, String type, String jsonMapping);

    public boolean validateIndex(String index);

    public boolean updateIndexAlias(String index, String alias);

    public <E> String indexMapping(Mapping<T, E> mapping, T document);

    public <E> String indexMappingAndRefresh(Mapping<T, E> mapping, T document);

    public <E> IndexResponse<E> bulkOperation(Mapping<T, E> mapping, List<IdentityType> documents);

    public <E> void removeChildMapping(String id, ChildMapping<T, E> mapping);

    public <E> String indexChildMapping(ChildMapping<T, E> mapping, T document);

    public <E> String updateChildMapping(String id, ChildMapping<T, E> mapping, T document);

    public <E> String updateMapping(String id, Mapping<T, E> mapping, T document);

    public <E> SearchResponse<E> find(String id, Mapping<T, E> mapping);

    public <E> SearchResponse<E> findByAlias(String id, Mapping<T, E> mapping);

    public <E> SearchResponse<E> queryByIds(Mapping<T, E> mapping, String sortField, List<String> ids);

    public <E> SearchResponse<E> queryByField(Mapping<T, E> mapping, int from, int size, String sortField,
            String serchField, String searchValue);

    public <E> SearchResponse<E> queryByFields(Mapping<T, E> mapping, int from, int size, String sortField,
            boolean sortOrder, Map<String, String> fieldValues, List<TermFieldFilter> termFilters,
            List<RangeFilter> rangeFilters, List<ShouldConditionsFilter> shouldFilters,
            List<ShouldConditionsFilter> shouldTermsConditionsFilters,
            List<ShouldConditionsFilter> shouldConditionsByRegexpFilters);

    public <E> SearchResponse<E> queryByFieldsAndAggs(Mapping<T, E> mapping, Map<String, String> fieldValues,
            List<RangeFilter> rangeFilters, List<ShouldConditionsFilter> shouldFilters,
            List<ShouldConditionsFilter> shouldTermsConditionsFilters,
            List<TermFieldFilter> termFilters, String aggregationField, boolean findMissing);

    public <E> SearchResult queryByFieldsAsJson(Mapping<T, E> mapping, int from, int size, String sortField,
            boolean sortOrder, Map<String, String> fieldValues, List<TermFieldFilter> termFilters,
            List<RangeFilter> rangeFilters, List<ShouldConditionsFilter> shouldFilters,
            List<ShouldConditionsFilter> shouldTermsConditionsFilters,
            List<ShouldConditionsFilter> shouldConditionsByRegexpFilters);

    public <E> SearchResponse<E> queryMatchAll(int from, int size, String sortField, boolean sortOrder,
            Mapping<T, E> mapping);

    public <E> SearchResult queryMatchAllAsJson(int from, int size, String sortField, boolean sortOrder,
            Mapping<T, E> mapping);

    public <E> SearchResponse<E> searchChildMapping(int from, int size, String sortField, ChildMapping<T, E> mapping);

    public <E> SearchResponse<E> searchChildMapping(ChildMapping<T, E> mapping);

    public <E> SearchResponse<E> searchChildMappingWithFilters(int from, int size, String sortField,
            QueryBuilder filters, ChildMapping<T, E> mapping);

    public <E> SearchResponse<E> searchChildMappingWithFilters(QueryBuilder filters, ChildMapping<T, E> mapping);

    public <E> SearchResponse<E> searchWithFilters(Integer from, Integer size, String sortField, SortOrder sortOrder,
            BoolQueryBuilder filters, Mapping<T, E> mapping);

    public <E> SearchResponse<E> searchWithFilters(String sortField, SortOrder sortOrder, BoolQueryBuilder filters,
            Mapping<T, E> mapping);

    public <E> SearchResponse<E> searchWithFilters(BoolQueryBuilder filters, Mapping<T, E> mapping);

    public <E> SearchResponse<E> search(Mapping<T, E> mapping);

    public <E> SearchResponse<E> search(Integer from, Integer size, String sortField, Mapping<T, E> mapping);

    public <E> void removeMapping(String id, Mapping<T, E> mapping);

}
