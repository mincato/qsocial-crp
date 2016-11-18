package com.qsocialnow.elasticsearch.services.cases;

import com.qsocialnow.common.model.cases.Person;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.cases.PersonMapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.PersonType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class PersonService extends CaseIndexService {

    public PersonService(AWSElasticsearchConfigurationProvider configurationProvider) {
        super(configurationProvider);
        initIndex();
    }

    public String indexPerson(Person document) {

        RepositoryFactory<PersonType> esfactory = new RepositoryFactory<PersonType>(elasticSearchCaseConfigurator);

        Repository<PersonType> repository = esfactory.initManager();
        repository.initClient();

        PersonMapping mapping = PersonMapping.getInstance();
        mapping.setIndex(this.getIndex(repository));

        // index document
        PersonType documentIndexed = mapping.getDocumentType(document);

        String personId = repository.indexMappingAndRefresh(mapping, documentIndexed);
        repository.closeClient();
        return personId;
    }

    public Person findPersonById(String personId) {
        RepositoryFactory<PersonType> esfactory = new RepositoryFactory<PersonType>(elasticSearchCaseConfigurator);
        Repository<PersonType> repository = esfactory.initManager();
        repository.initClient();

        PersonMapping mapping = PersonMapping.getInstance();

        String indexName = this.getQueryIndex();
        mapping.setIndex(indexName);

        // index document
        SearchResponse<Person> searchResponse = repository.findByAlias(personId, mapping);
        Person response = searchResponse.getSource();

        repository.closeClient();
        return response;
    }

    public String update(Person document) {
        RepositoryFactory<PersonType> esfactory = new RepositoryFactory<PersonType>(elasticSearchCaseConfigurator);
        Repository<PersonType> repository = esfactory.initManager();
        repository.initClient();

        PersonMapping mapping = PersonMapping.getInstance();

        String indexName = this.getQueryIndex();
        mapping.setIndex(indexName);

        // searching to retrieve index value
        SearchResponse<Person> indexResponse = repository.findByAlias(document.getId(), mapping);
        mapping.setIndex(indexResponse.getIndex());

        // index document
        PersonType documentIndexed = mapping.getDocumentType(document);
        String response = repository.updateMapping(document.getId(), mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

}
