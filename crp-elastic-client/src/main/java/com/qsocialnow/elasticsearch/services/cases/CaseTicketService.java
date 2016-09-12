package com.qsocialnow.elasticsearch.services.cases;

import java.time.LocalDateTime;
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

public class CaseTicketService {

    private static final Logger log = LoggerFactory.getLogger(CaseService.class);

    private final static String INDEX_NAME = "cases_";

    private static AWSElasticsearchConfigurationProvider elasticSearchCaseConfigurator;

    public CaseTicketService(AWSElasticsearchConfigurationProvider configurationProvider) {
        elasticSearchCaseConfigurator = configurationProvider;
    }

    public Case findCaseById(String originIdCase) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        mapping.setIndex(INDEX_NAME + generateIndexValue());
        SearchResponse<Case> response = repository.find(originIdCase, mapping);

        Case caseDocument = response.getSource();
        log.info("Retrieving from ES Case:" + caseDocument.getId());
        repository.closeClient();
        return caseDocument;
    }

    public List<Case> getCases(int from, int size) {

        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        SearchResponse<Case> response = repository.search(from, size, "openDate", mapping);

        List<Case> cases = response.getSources();

        repository.closeClient();
        return cases;
    }

    private String generateIndexValue() {
        String indexSufix = null;

        LocalDateTime dateTime = LocalDateTime.now();
        int month = dateTime.getMonthValue();
        int year = dateTime.getYear();
        indexSufix = year + "_" + month;

        return indexSufix;
    }
}
