package com.qsocialnow.elasticsearch.services.cases;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.model.cases.ActionRegistry;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.cases.ActionRegistryMapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.ActionRegistryType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class ActionRegistryService extends CaseIndexService {

    private static final Logger log = LoggerFactory.getLogger(ActionRegistryService.class);

    private static AWSElasticsearchConfigurationProvider elasticSearchCaseConfigurator;

    public ActionRegistryService(AWSElasticsearchConfigurationProvider configurationProvider) {
        elasticSearchCaseConfigurator = configurationProvider;
    }

    public String indexRegistry(String idCase, ActionRegistry document) {

        RepositoryFactory<ActionRegistryType> esfactory = new RepositoryFactory<ActionRegistryType>(
                elasticSearchCaseConfigurator);
        Repository<ActionRegistryType> repository = esfactory.initManager();
        repository.initClient();

        ActionRegistryMapping mapping = ActionRegistryMapping.getInstance();
        mapping.setIndex(this.getIndex(repository));
        log.info("Indexing new registry from case: " + idCase);
        // index document
        ActionRegistryType documentIndexed = mapping.getDocumentType(document);
        documentIndexed.setIdCase(idCase);
        String response = repository.indexMappingAndRefresh(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public List<ActionRegistry> findRegistries(int from, int size, String caseId) {

        RepositoryFactory<ActionRegistryType> esfactory = new RepositoryFactory<ActionRegistryType>(
                elasticSearchCaseConfigurator);
        Repository<ActionRegistryType> repository = esfactory.initManager();
        repository.initClient();

        ActionRegistryMapping mapping = ActionRegistryMapping.getInstance();
        mapping.setIndex(this.getQueryIndex());

        log.info("Retriving registries from case: " + caseId);
        SearchResponse<ActionRegistry> response = repository
                .queryByField(mapping, from, size, "date", "idCase", caseId);

        List<ActionRegistry> registries = response.getSources();

        repository.closeClient();
        return registries;
    }

    public List<ActionRegistry> findRegistriesBy(int from, int size, String caseId, String textValue, String action,
            String user, String fromDate, String toDate) {

        RepositoryFactory<ActionRegistryType> esfactory = new RepositoryFactory<ActionRegistryType>(
                elasticSearchCaseConfigurator);
        Repository<ActionRegistryType> repository = esfactory.initManager();
        repository.initClient();

        ActionRegistryMapping mapping = ActionRegistryMapping.getInstance();
        mapping.setIndex(this.getQueryIndex());
        Map<String, String> searchValues = new HashMap<>();
        searchValues.put("idCase", caseId);

        if (textValue != null)
            searchValues.put("comment", textValue);
        if (action != null)
            searchValues.put("action", action);
        if (user != null)
            searchValues.put("user", user);

        SearchResponse<ActionRegistry> response = repository.queryByFields(mapping, from, size, "action", searchValues,
                "date", fromDate, toDate);
        List<ActionRegistry> registries = response.getSources();

        repository.closeClient();
        return registries;
    }

    public List<ActionRegistry> findRegistryByEventId(String caseId, String eventId) {
        RepositoryFactory<ActionRegistryType> esfactory = new RepositoryFactory<ActionRegistryType>(
                elasticSearchCaseConfigurator);
        Repository<ActionRegistryType> repository = esfactory.initManager();
        repository.initClient();

        ActionRegistryMapping mapping = ActionRegistryMapping.getInstance();
        mapping.setIndex(this.getQueryIndex());

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.matchQuery("idCase", caseId));
        if (eventId != null) {
            filters = filters.must(QueryBuilders.matchQuery("event.id", eventId));
        }

        SearchResponse<ActionRegistry> response = repository.searchWithFilters(filters, mapping);
        List<ActionRegistry> registries = response.getSources();

        repository.closeClient();
        return registries;
    }
}
