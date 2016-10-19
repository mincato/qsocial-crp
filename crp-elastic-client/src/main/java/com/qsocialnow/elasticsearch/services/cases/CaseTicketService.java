package com.qsocialnow.elasticsearch.services.cases;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.cases.CaseMapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.CaseType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

import io.searchbox.core.SearchResult;

public class CaseTicketService extends CaseIndexService {

    private static final Logger log = LoggerFactory.getLogger(CaseTicketService.class);

    public CaseTicketService(AWSElasticsearchConfigurationProvider configurationProvider) {
        super(configurationProvider);
        initIndex();
    }

    public Case findCaseById(String originIdCase) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        mapping.setIndex(getQueryIndex());
        SearchResponse<Case> response = repository.findByAlias(originIdCase, mapping);

        Case caseDocument = response.getSource();
        repository.closeClient();
        return caseDocument;
    }

    public List<Case> getCases(int from, int size, String sortField, boolean sortOrder, String subject, String title,
            String description, String pendingResponse, String fromOpenDate, String toOpenDate,
            List<String> teamsToFilter, String userName) {
    	
    	this.initIndex();
    	
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        log.info("retrieving cases from :" + from + " size" + size + " sorted by;" + sortField);

        Map<String, String> searchValues = new HashMap<>();

        if (userName != null) {
            searchValues.put("assignee.username", userName);
        }

        if (subject != null)
            searchValues.put("subject.identifier", subject);

        if (title != null)
            searchValues.put("title", title);

        if (description != null)
            searchValues.put("description", description);

        if (pendingResponse != null)
            searchValues.put("pendingResponse", pendingResponse);

        SearchResponse<Case> response = repository.queryByFields(mapping, from, size, sortField,
                Boolean.valueOf(sortOrder), searchValues, "openDate", fromOpenDate, toOpenDate);

        List<Case> cases = response.getSources();

        repository.closeClient();
        return cases;
    }

    public JsonObject getCasesAsJsonObject(int from, int size, String sortField, boolean sortOrder, String title,
            String description, String pendingResponse, String fromOpenDate, String toOpenDate) {

        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        log.info("retrieving cases from :" + from + " size" + size + " sorted by;" + sortField);

        Map<String, String> searchValues = new HashMap<>();

        if (title != null)
            searchValues.put("title", title);

        if (description != null)
            searchValues.put("description", description);

        if (pendingResponse != null)
            searchValues.put("pendingResponse", pendingResponse);

        SearchResult response = repository.queryByFieldsAsJson(mapping, from, size, sortField,
                Boolean.valueOf(sortOrder), searchValues, "openDate", fromOpenDate, toOpenDate);

        JsonObject jsonObject = response.getJsonObject();
        repository.closeClient();
        return jsonObject;
    }

    public String indexCase(Case document) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();

        String indexName = this.getIndex(repository);
        mapping.setIndex(indexName);

        // index document
        document.setLastModifiedTimestamp(new Date().getTime());
        CaseType documentIndexed = mapping.getDocumentType(document);
        String response = repository.indexMapping(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public String update(Case document) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();

        String indexName = this.getQueryIndex();
        mapping.setIndex(indexName);

        // searching to retrieve index value
        SearchResponse<Case> indexResponse = repository.findByAlias(document.getId(), mapping);
        mapping.setIndex(indexResponse.getIndex());

        // index document
        document.setLastModifiedTimestamp(new Date().getTime());
        CaseType documentIndexed = mapping.getDocumentType(document);
        String response = repository.updateMapping(document.getId(), mapping, documentIndexed);
        repository.closeClient();
        return response;
    }
}
