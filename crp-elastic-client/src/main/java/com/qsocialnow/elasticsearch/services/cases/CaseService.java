package com.qsocialnow.elasticsearch.services.cases;

import java.time.LocalDateTime;
import java.util.List;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.elasticsearch.configuration.CaseConfigurator;
import com.qsocialnow.elasticsearch.configuration.ConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.cases.CaseMapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.CaseType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class CaseService {

    private final static String INDEX_NAME = "cases_";

    public String indexCase(Case document) {
        CaseConfigurator configurator = new CaseConfigurator();
        return indexCase(configurator, document);
    }

    public String indexCase(ConfigurationProvider configurator, Case document) {

        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(configurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();

        String indexName = INDEX_NAME + generateIndexValue();
        mapping.setIndex(indexName);

        // validete index name
        boolean isCreated = repository.validateIndex(indexName);
        // create index
        if (!isCreated) {
            repository.createIndex(mapping.getIndex());
        }
        // index document
        CaseType documentIndexed = mapping.getDocumentType(document);
        String response = repository.indexMapping(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public List<Case> getCases(int from, int size) {

        CaseConfigurator configurator = new CaseConfigurator();
        return getCases(configurator, from, size);
    }

    public List<Case> getCases(ConfigurationProvider configurator, int from, int size) {

        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(configurator);
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
        int day = dateTime.getDayOfMonth();
        int month = dateTime.getMonthValue();
        int year = dateTime.getYear();
        int hour = dateTime.getHour();

        indexSufix = year + "_" + month + "_" + day + "_" + hour;

        return indexSufix;
    }

}
