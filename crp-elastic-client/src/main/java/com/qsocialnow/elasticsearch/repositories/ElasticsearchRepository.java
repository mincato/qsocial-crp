package com.qsocialnow.elasticsearch.repositories;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;
import com.qsocialnow.common.exception.RepositoryException;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.mappings.ChildMapping;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.IdentityType;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.aliases.AddAliasMapping;
import io.searchbox.indices.aliases.ModifyAliases;
import io.searchbox.indices.mapping.PutMapping;
import vc.inreach.aws.request.AWSSigner;
import vc.inreach.aws.request.AWSSigningRequestInterceptor;

public class ElasticsearchRepository<T> implements Repository<T> {

    private static final String PARENT_PARAMETER = "parent";

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchRepository.class);

    private AWSElasticsearchConfigurationProvider config;

    private JestClient client;

    private AWSSigningRequestInterceptor requestInterceptor;

    public ElasticsearchRepository(AWSElasticsearchConfigurationProvider config) {
        this.config = config;
    }

    public void initClient() {
        if (client == null) {

            AWSSigner signer = getSigner();
            requestInterceptor = new AWSSigningRequestInterceptor(signer);

            JestClientFactory factory = new JestClientFactory() {

                @Override
                protected HttpClientBuilder configureHttpClient(HttpClientBuilder builder) {
                    builder.addInterceptorLast(requestInterceptor);
                    return builder;
                }

                @Override
                protected HttpAsyncClientBuilder configureHttpClient(HttpAsyncClientBuilder builder) {
                    builder.addInterceptorLast(requestInterceptor);
                    return builder;
                }
            };
            factory.setHttpClientConfig(new HttpClientConfig.Builder(this.config.getEndpoint()).multiThreaded(true)
                    .build());
            this.client = factory.getObject();
        }
    }

    public void closeClient() {
        if (client == null) {
            client.shutdownClient();
        }
    }

    private AWSSigner getSigner() {
        Supplier<LocalDateTime> clock = () -> LocalDateTime.now(ZoneOffset.UTC);
        AWSSigner awsSigner = new AWSSigner(this.config, this.config.getRegion(), Configurator.SERVICE_NAME, clock);
        return awsSigner;
    }

    public void createIndex(String index) {
        try {
            client.execute(new CreateIndex.Builder(index).build());
        } catch (IOException e) {
            log.error("Unexpected error: ", e);

        }
    }

    public boolean validateIndex(String index) {

        boolean indexExists = false;
        try {
            IndicesExists action = new IndicesExists.Builder(index).build();
            JestResult result = client.execute(action);
            indexExists = result.isSucceeded();
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
        }
        return indexExists;
    }

    public boolean updateIndexAlias(String index, String alias) {
        boolean indexExists = false;
        try {
            AddAliasMapping addAliasMapping = new AddAliasMapping.Builder(index, alias).build();

            ModifyAliases modifyAliases = new ModifyAliases.Builder(addAliasMapping).build();
            JestResult result = client.execute(modifyAliases);
            indexExists = result.isSucceeded();
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
        }
        return indexExists;
    }

    public void deleteIndex(String index) {
        DeleteIndex deleteIndex = new DeleteIndex.Builder(index).build();
        try {
            client.execute(deleteIndex);
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
        }
    }

    @Override
    public void createMapping(String index, String type, String jsonMapping) {
        PutMapping putMapping = new PutMapping.Builder(index, type, jsonMapping).build();
        try {
            client.execute(putMapping);
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
        }
    }

    @Override
    public <E> String indexMapping(Mapping<T, E> mapping, T document) {
        Index index = new Index.Builder(document).index(mapping.getIndex()).type(mapping.getType()).build();
        String idValue = null;
        try {
            DocumentResult response = client.execute(index);
            if (response.isSucceeded()) {
                idValue = response.getId();
            } else {
                log.error("There was an error indexing mapping: " + response.getErrorMessage());
                throw new RepositoryException(response.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            throw new RepositoryException(e);
        }
        return idValue;
    }

    @Override
    public <E> String indexMappingAndRefresh(Mapping<T, E> mapping, T document) {
        Index index = new Index.Builder(document).index(mapping.getIndex()).type(mapping.getType()).refresh(true)
                .build();
        String idValue = null;
        try {
            DocumentResult response = client.execute(index);
            if (response.isSucceeded()) {
                idValue = response.getId();
            } else {
                log.error("There was an error indexing mapping: " + response.getErrorMessage());
                throw new RepositoryException(response.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            throw new RepositoryException(e);
        }
        return idValue;
    }

    public <E> IndexResponse<E> bulkOperation(Mapping<T, E> mapping, List<IdentityType> documents) {

        List<Index> modelList = new ArrayList<Index>();
        // TODO:index by alias
        for (IdentityType identity : documents) {
            Index indexDocument;
            if (identity.getId() != null) {
                log.info("Updating case: " + identity.getId());
                indexDocument = new Index.Builder(identity).index(mapping.getIndex()).type(mapping.getType())
                        .id(identity.getId()).build();
            } else {
                indexDocument = new Index.Builder(identity).index(mapping.getIndex()).type(mapping.getType()).build();
            }
            modelList.add(indexDocument);
        }

        Bulk bulk = new Bulk.Builder().addAction(modelList).build();

        IndexResponse<E> response = new IndexResponse<>();
        try {
            BulkResult result = client.execute(bulk);

            if (result.isSucceeded()) {
                response.setSourcesBulk(result.getItems());
                response.setSucceeded(true);
            } else {
                response.setSucceeded(false);
                response.setFailedItems(result.getFailedItems());
            }
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
        }
        return response;
    }

    @Override
    public <E> String indexChildMapping(ChildMapping<T, E> mapping, T document) {
        Index index = new Index.Builder(document).index(mapping.getIndex()).type(mapping.getType())
                .setParameter(PARENT_PARAMETER, mapping.getIdParent()).build();
        String idValue = null;
        try {
            DocumentResult response = client.execute(index);
            if (response.isSucceeded()) {
                idValue = response.getId();
            } else {
                log.error("There was an error indexing child mapping: " + response.getErrorMessage());
                throw new RepositoryException(response.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            throw new RepositoryException(e);
        }
        return idValue;
    }

    @Override
    public <E> String updateChildMapping(String id, ChildMapping<T, E> mapping, T document) {
        Index index = new Index.Builder(document).index(mapping.getIndex()).type(mapping.getType()).id(id)
                .setParameter(PARENT_PARAMETER, mapping.getIdParent()).build();
        String idValue = null;
        try {
            DocumentResult response = client.execute(index);
            if (response.isSucceeded()) {
                idValue = response.getId();
            } else {
                log.error("There was an error indexing child mapping: " + response.getErrorMessage());
                throw new RepositoryException(response.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            throw new RepositoryException(e);
        }
        return idValue;
    }

    @Override
    public <E> void removeChildMapping(String id, ChildMapping<T, E> mapping) {
        Delete delete = new Delete.Builder(id).index(mapping.getIndex()).type(mapping.getType())
                .setParameter(PARENT_PARAMETER, mapping.getIdParent()).build();
        try {
            DocumentResult response = client.execute(delete);
            if (!response.isSucceeded()) {
                log.error("There was an error removing mapping: " + response.getErrorMessage());
                throw new RepositoryException(response.getErrorMessage());
            } else {
            }
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            throw new RepositoryException(e);
        }
    }

    @Override
    public <E> String updateMapping(String id, Mapping<T, E> mapping, T document) {

        Index update = new Index.Builder(document).index(mapping.getIndex()).type(mapping.getType()).id(id)
                .refresh(true).build();
        String idValue = null;
        try {
            DocumentResult response = client.execute(update);
            if (response.isSucceeded()) {
                idValue = response.getId();
            } else {
                throw new RepositoryException(response.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            throw new RepositoryException(e);
        }
        return idValue;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public <E> SearchResponse<E> queryByField(Mapping<T, E> mapping, int from, int size, String sortField,
            String searchField, String searchValue) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (size > 0)
            searchSourceBuilder.from(from).size(size);

        if (sortField != null)
            searchSourceBuilder.sort(sortField, SortOrder.ASC);

        searchSourceBuilder.query(QueryBuilders.matchQuery(searchField, searchValue));

        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(mapping.getIndex())
                .addType(mapping.getType()).build();
        return executeSearch(mapping, search);
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public <E> SearchResponse<E> queryByFields(Mapping<T, E> mapping, int from, int size, String sortField,
            Map<String, String> searchValues, String rangeField, String fromValue, String toValue) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(from).size(size).sort(sortField, SortOrder.ASC);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (String searchField : searchValues.keySet()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery(searchField, searchValues.get(searchField)));
        }

        if (rangeField != null) {
            RangeQueryBuilder range = QueryBuilders.rangeQuery(rangeField);
            if (fromValue != null)
                range.gte(fromValue);
            if (toValue != null)
                range.lte(toValue);

            boolQueryBuilder.filter(range);
        }
        searchSourceBuilder.query(boolQueryBuilder);

        log.info("Query: " + searchSourceBuilder.toString());

        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(mapping.getIndex())
                .addType(mapping.getType()).build();
        return executeSearch(mapping, search);
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public <E> SearchResponse<E> queryMatchAll(int from, int size, String sortField, Mapping<T, E> mapping) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(from).size(size).sort(sortField, SortOrder.ASC).query(QueryBuilders.matchAllQuery());
        Search search = new Search.Builder(searchSourceBuilder.toString()).addType(mapping.getType()).build();
        return executeSearch(mapping, search);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> SearchResponse<E> find(String id, Mapping<T, E> mapping) {
        Get get = new Get.Builder(mapping.getIndex(), id).type(mapping.getType()).build();
        log.info("Elasticsearch get document with id" + id);
        DocumentResult result = null;
        SearchResponse<E> response = new SearchResponse<E>();
        try {
            result = client.execute(get);
            if (result.isSucceeded()) {
                T source = (T) result.getSourceAsObject(mapping.getClassType());
                response.setSource(mapping.getDocument(source));
            }
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            throw new RepositoryException(e);
        }
        return response;
    }

    @Override
    @SuppressWarnings({ "unchecked", "deprecation" })
    public <E> SearchResponse<E> findByAlias(String id, Mapping<T, E> mapping) {
        log.info("Elasticsearch get document with id:" + id);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id", id));
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(mapping.getIndex())
                .addType(mapping.getType()).build();
        SearchResult result = null;
        SearchResponse<E> response = new SearchResponse<E>();
        try {
            result = client.execute(search);
            if (result.isSucceeded()) {
                response.setSource(mapping.getDocument((T) result.getSourceAsObject(mapping.getClassType())));
                String indexName = result.getJsonObject().getAsJsonObject("hits").getAsJsonArray("hits").get(0)
                        .getAsJsonObject().get("_index").getAsString();
                response.setIndex(indexName);
            } else {
                throw new RepositoryException(result.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("Serching documents - Unexpected error: ", e);
            throw new RepositoryException(e);
        }
        return response;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    private <E> SearchResponse<E> executeSearch(Mapping<T, E> mapping, Search search) {
        SearchResult result = null;
        SearchResponse<E> response = new SearchResponse<E>();
        try {
            result = client.execute(search);
            if (result.isSucceeded()) {
                List<T> responses = (List<T>) result.getSourceAsObjectList(mapping.getClassType());
                response.setSources(responses.stream().map(elasticDocument -> mapping.getDocument(elasticDocument))
                        .collect(Collectors.toList()));
            } else {
                throw new RepositoryException(result.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("Serching documents - Unexpected error: ", e);
            throw new RepositoryException(e);
        }
        return response;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public <E> SearchResponse<E> searchChildMapping(int from, int size, String sortField, ChildMapping<T, E> mapping) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .from(from)
                .size(size)
                .sort(sortField, SortOrder.ASC)
                .query(QueryBuilders.hasParentQuery(mapping.getParentType(),
                        QueryBuilders.termQuery("_id", mapping.getIdParent())));
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(mapping.getIndex())
                .addType(mapping.getType()).build();

        SearchResult result = null;
        SearchResponse<E> response = new SearchResponse<E>();
        try {
            result = client.execute(search);

        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            throw new RepositoryException(e);
        }

        if (result.isSucceeded()) {
            List<T> responses = (List<T>) result.getSourceAsObjectList(mapping.getClassType());
            response.setSources(responses.stream().map(elasticDocument -> mapping.getDocument(elasticDocument))
                    .collect(Collectors.toList()));
        } else {
            throw new RepositoryException(result.getErrorMessage());
        }
        return response;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public <E> SearchResponse<E> searchChildMappingWithFilters(int from, int size, String sortField,
            QueryBuilder filters, ChildMapping<T, E> mapping) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .from(from)
                .size(size)
                .sort(sortField, SortOrder.ASC)
                .query(QueryBuilders
                        .boolQuery()
                        .must(filters)
                        .must(QueryBuilders.hasParentQuery(mapping.getParentType(),
                                QueryBuilders.termQuery("_id", mapping.getIdParent()))));
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(mapping.getIndex())
                .addType(mapping.getType()).build();

        SearchResult result = null;
        SearchResponse<E> response = new SearchResponse<E>();
        try {
            result = client.execute(search);

        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            throw new RepositoryException(e);
        }

        if (result.isSucceeded()) {
            List<T> responses = (List<T>) result.getSourceAsObjectList(mapping.getClassType());
            response.setSources(responses.stream().map(elasticDocument -> mapping.getDocument(elasticDocument))
                    .collect(Collectors.toList()));
        } else {
            throw new RepositoryException(result.getErrorMessage());
        }
        return response;
    }

    @Override
    public <E> SearchResponse<E> searchChildMapping(ChildMapping<T, E> mapping) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.hasParentQuery(mapping.getParentType(),
                QueryBuilders.termQuery("_id", mapping.getIdParent())));
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(mapping.getIndex())
                .addType(mapping.getType()).build();

        SearchResult result = null;
        SearchResponse<E> response = new SearchResponse<E>();
        try {
            result = client.execute(search);

        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            throw new RepositoryException(e);
        }

        if (result.isSucceeded()) {
            List<T> responses = (List<T>) result.getSourceAsObjectList(mapping.getClassType());
            response.setSources(responses.stream().map(elasticDocument -> mapping.getDocument(elasticDocument))
                    .collect(Collectors.toList()));
        } else {
            throw new RepositoryException(result.getErrorMessage());
        }
        return response;
    }

    @Override
    public <E> SearchResponse<E> search(Integer from, Integer size, String sortField, Mapping<T, E> mapping) {
        return searchWithFilters(from, size, sortField, null, mapping);
    }

    @Override
    public <E> SearchResponse<E> searchWithFilters(Integer from, Integer size, String sortField,
            BoolQueryBuilder filters, Mapping<T, E> mapping) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        if (from != null) {
            searchSourceBuilder.from(from);
        }
        if (size != null) {
            searchSourceBuilder.size(size);
        }
        if (sortField != null) {
            searchSourceBuilder.sort(sortField, SortOrder.DESC);
        }
        if (filters != null) {
            searchSourceBuilder.query(filters);
        }
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(mapping.getIndex())
                .addType(mapping.getType()).build();

        SearchResult result = null;
        SearchResponse<E> response = new SearchResponse<E>();
        try {
            result = client.execute(search);

        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            throw new RepositoryException(e);
        }

        if (result.isSucceeded()) {
            List<T> responses = (List<T>) result.getSourceAsObjectList(mapping.getClassType());
            response.setSources(responses.stream().map(elasticDocument -> mapping.getDocument(elasticDocument))
                    .collect(Collectors.toList()));
        } else {
            throw new RepositoryException(result.getErrorMessage());
        }
        return response;
    }

    @Override
    public <E> SearchResponse<E> searchWithFilters(BoolQueryBuilder filters, Mapping<T, E> mapping) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(filters);
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(mapping.getIndex())
                .addType(mapping.getType()).build();

        SearchResult result = null;
        SearchResponse<E> response = new SearchResponse<E>();
        try {
            result = client.execute(search);

        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            throw new RepositoryException(e);
        }

        if (result.isSucceeded()) {
            List<T> responses = (List<T>) result.getSourceAsObjectList(mapping.getClassType());
            response.setSources(responses.stream().map(elasticDocument -> mapping.getDocument(elasticDocument))
                    .collect(Collectors.toList()));
        } else {
            throw new RepositoryException(result.getErrorMessage());
        }
        return response;
    }

    @Override
    public <E> void removeMapping(String id, Mapping<T, E> mapping) {
        Delete delete = new Delete.Builder(id).index(mapping.getIndex()).type(mapping.getType()).build();
        try {
            DocumentResult response = client.execute(delete);
            if (!response.isSucceeded()) {
                log.error("There was an error removing mapping: " + response.getErrorMessage());
                throw new RepositoryException(response.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
            throw new RepositoryException(e);
        }

    }

}
