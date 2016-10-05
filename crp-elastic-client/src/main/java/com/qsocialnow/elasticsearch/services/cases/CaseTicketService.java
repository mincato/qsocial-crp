package com.qsocialnow.elasticsearch.services.cases;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.cases.CaseMapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.CaseType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class CaseTicketService extends DynamicIndexService {

    private static final Logger log = LoggerFactory.getLogger(CaseTicketService.class);

    private static AWSElasticsearchConfigurationProvider elasticSearchCaseConfigurator;

    public CaseTicketService(AWSElasticsearchConfigurationProvider configurationProvider) {
        elasticSearchCaseConfigurator = configurationProvider;
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

    public List<Case> getCases(int from, int size) {

        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        SearchResponse<Case> response = repository.queryMatchAll(from, size, "openDate", mapping);

        List<Case> cases = response.getSources();

        repository.closeClient();
        return cases;
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
