package com.qsocialnow.elasticsearch.services.config;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.TriggerMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.TriggerType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class TriggerService {

    private AWSElasticsearchConfigurationProvider configurator;

    public String indexTrigger(String domainId, Trigger trigger) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);

        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance();

        // validete index name
        boolean isCreated = repository.validateIndex(mapping.getIndex());
        // create index
        if (!isCreated) {
            repository.createIndex(mapping.getIndex());
        }

        // index document
        TriggerType documentIndexed = mapping.getDocumentType(trigger);
        documentIndexed.setDomainId(domainId);
        String response = repository.indexMapping(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public List<Trigger> getTriggers(String domainId, Integer offset, Integer limit, String name) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);
        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance();

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.matchQuery("domainId", domainId));
        if (name != null) {
            filters = filters.must(QueryBuilders.matchQuery("name", name));
        }
        SearchResponse<Trigger> response = repository.searchWithFilters(offset, limit, "name", filters, mapping);
        List<Trigger> triggers = response.getSources();

        repository.closeClient();
        return triggers;
    }

    public List<Trigger> getTriggers(String domainId) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);
        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance();

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.matchQuery("domainId", domainId));
        SearchResponse<Trigger> response = repository.searchWithFilters(filters, mapping);

        List<Trigger> triggers = response.getSources();

        repository.closeClient();
        return triggers;

    }

    public Trigger findOne(String triggerId) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);
        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance();

        SearchResponse<Trigger> response = repository.find(triggerId, mapping);

        Trigger triggers = response.getSource();

        repository.closeClient();
        return triggers;
    }

    public void setConfigurator(AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

}
