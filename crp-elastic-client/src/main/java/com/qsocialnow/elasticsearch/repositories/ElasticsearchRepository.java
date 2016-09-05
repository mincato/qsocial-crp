package com.qsocialnow.elasticsearch.repositories;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;
import com.qsocialnow.elasticsearch.configuration.ConfigurationProvider;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.mappings.ChildMapping;
import com.qsocialnow.elasticsearch.mappings.Mapping;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.PutMapping;
import vc.inreach.aws.request.AWSSigner;
import vc.inreach.aws.request.AWSSigningRequestInterceptor;

public class ElasticsearchRepository<T> implements Repository<T> {

    private static final String PARENT_PARAMETER = "parent";

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchRepository.class);

    private ConfigurationProvider config;

    private JestClient client;

    private AWSSigningRequestInterceptor requestInterceptor;

    public ElasticsearchRepository(ConfigurationProvider config) {
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

    @Override
    public <E> String indexMapping(Mapping<T, E> mapping, T document) {
        Index index = new Index.Builder(document).index(mapping.getIndex()).type(mapping.getType()).build();
        String idValue = null;
        try {
            DocumentResult response = client.execute(index);
            if (response.isSucceeded()) {
                idValue = response.getId();
            }
        } catch (IOException e) {
            log.error("Unexpected error: ", e);

        }
        return idValue;
    }

    @SuppressWarnings("unchecked")
    public <E> IndexResponse<E> bulkOperation(Mapping<T, E> mapping, List<T> documents) {

        List<Index> modelList = new ArrayList<Index>();
        for (T t : documents) {
            modelList.add(new Index.Builder(t).build());
        }

        Bulk bulk = new Bulk.Builder().defaultIndex(mapping.getIndex()).defaultType(mapping.getType())
                .addAction(modelList).build();

        IndexResponse<E> response = new IndexResponse<>();
        try {
            BulkResult result = client.execute(bulk);

            if (result.isSucceeded()) {
                response.setSourcesBulk(result.getItems());
                response.setSucceeded(true);
            } else {
                // TODO:implement processing error
                response.setSucceeded(false);
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
                throw new RuntimeException(response.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("Unexpected error: ", e);

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
                throw new RuntimeException(response.getErrorMessage());
            }
        } catch (IOException e) {
            log.error("Unexpected error: ", e);

        }
        return idValue;
    }

    @Override
    public <E> String updateIndexMapping(String id, Mapping<T, E> mapping, T document) {

        Index update = new Index.Builder(document).index(mapping.getIndex()).type(mapping.getType()).id(id).build();
        String idValue = null;
        try {
            DocumentResult response = client.execute(update);
            if (response.isSucceeded()) {
                idValue = response.getId();
            }
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
        }
        return idValue;
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

    public void deleteIndex(String index) {
        DeleteIndex deleteIndex = new DeleteIndex.Builder(index).build();

        try {
            client.execute(deleteIndex);
        } catch (IOException e) {
            log.error("Unexpected error: ", e);

        }
    }

    public <E> void createMapping(Mapping<T, E> mapping) {
        PutMapping putMapping = new PutMapping.Builder(mapping.getIndex(), mapping.getType(),
                mapping.getMappingDefinition()).build();
        try {
            client.execute(putMapping);
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
        }
    }

    @SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
    public <E> SearchResponse<E> query(Mapping<T, E> mapping, String searchValue) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("name", searchValue));

        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(mapping.getIndex())
                .addType(mapping.getType()).build();

        SearchResult result = null;
        try {
            result = client.execute(search);

        } catch (IOException e) {
            log.error("Unexpected error: ", e);
        }
        SearchResponse<E> response = new SearchResponse<E>();
        if (result.isSucceeded()) {
            // retrieve metadata value
            List<Hit<Map, Void>> hits = result.getHits(Map.class);
            Hit<Map, Void> hit = hits.get(0);
            Map source = (Map) hit.source;
            String id = (String) source.get(JestResult.ES_METADATA_ID);

            response.setSource((E) result.getSourceAsObject(mapping.getClassType()));
            response.setId(id);
        }
        return response;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public <E> SearchResponse<E> search(int from, int size, String sortField, String name, Mapping<T, E> mapping) {

        String query = "{\"from\" :" + from + ", \"size\" : " + size + " ," + "\"sort\" : [{ \"" + sortField
                + "\" : {\"order\" : \"asc\"}}] ," + "\"query\":{ \"match_all\" : { }}}";

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("name", name));

        Search search = new Search.Builder(searchSourceBuilder.toString()).addType(mapping.getType()).build();

        SearchResult result = null;
        SearchResponse<E> response = new SearchResponse<E>();
        try {
            result = client.execute(search);

        } catch (IOException e) {
            log.error("Unexpected error: ", e);
        }

        if (result.isSucceeded()) {
            List<E> responses = (List<E>) result.getSourceAsObjectList(mapping.getClassType());
            response.setSources(responses);
        }
        return response;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public <E> SearchResponse<E> search(int from, int size, String sortField, Mapping<T, E> mapping) {

        String query = "{\"from\" :" + from + ", \"size\" : " + size + " ," + "\"sort\" : [{ \"" + sortField
                + "\" : {\"order\" : \"asc\"}}] ," + "\"query\":{ \"match_all\" : { }}}";

        Search search = new Search.Builder(query).addType(mapping.getType()).build();

        SearchResult result = null;
        SearchResponse<E> response = new SearchResponse<E>();
        try {
            result = client.execute(search);

        } catch (IOException e) {
            log.error("Unexpected error: ", e);
        }

        if (result.isSucceeded()) {
            List<E> responses = (List<E>) result.getSourceAsObjectList(mapping.getClassType());
            response.setSources(responses);
        }
        return response;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> SearchResponse<E> find(String id, Mapping<T, E> mapping) {
        Get get = new Get.Builder(mapping.getIndex(), id).type(mapping.getType()).build();

        DocumentResult result = null;
        SearchResponse<E> response = new SearchResponse<E>();
        try {
            result = client.execute(get);
        } catch (IOException e) {
            log.error("Unexpected error: ", e);
        }

        if (result.isSucceeded()) {
            response.setSource((E) result.getSourceAsObject(mapping.getClassType()));
            response.setId(result.getId());
        }
        return response;
    }
}
