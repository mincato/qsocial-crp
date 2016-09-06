package com.qsocialnow.elasticsearch.services.config;

import java.util.List;

import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.mappings.config.TriggerMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.TriggerType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class TriggerService {

    public String indexTrigger(Configurator elasticConfig, String domainId, Trigger trigger) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(elasticConfig);

        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance();
        mapping.setIdParent(domainId);

        // validete index name
        boolean isCreated = repository.validateIndex(mapping.getIndex());
        // create index
        if (!isCreated) {
            repository.createIndex(mapping.getIndex());
        }

        // index document
        TriggerType documentIndexed = mapping.getDocumentType(trigger);
        String response = repository.indexChildMapping(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public List<Trigger> getTriggers(Configurator configurator, String domainId, Integer offset, Integer limit) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);
        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance();
        mapping.setIdParent(domainId);

        SearchResponse<Trigger> response = repository.searchChildMapping(offset, limit, "name", mapping);

        List<Trigger> triggers = response.getSources();

        repository.closeClient();
        return triggers;
    }

    public List<Trigger> getTriggers(Configurator configurator, String domainId) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);
        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance();
        mapping.setIdParent(domainId);

        SearchResponse<Trigger> response = repository.searchChildMapping(mapping);

        List<Trigger> triggers = response.getSources();

        repository.closeClient();
        return triggers;

    }

}
