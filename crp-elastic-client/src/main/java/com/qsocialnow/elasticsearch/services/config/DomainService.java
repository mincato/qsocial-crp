package com.qsocialnow.elasticsearch.services.config;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.mappings.config.DomainMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.DomainType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class DomainService {

    public Domain findDomainById(String name) {
        Configurator configurator = new Configurator();
        return findDomainById(configurator, name);
    }

    public Domain findDomainById(Configurator configurator, String name) {

        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);
        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();

        DomainMapping mapping = DomainMapping.getInstance();

        SearchResponse<Domain> response = repository.query(mapping, name);

        Domain domain = response.getSource();
        domain.setId(response.getId());

        repository.closeClient();
        return domain;
    }

    public Domain findDomain(String id) {
        Configurator configurator = new Configurator();
        return findDomain(configurator, id);
    }

    public Domain findDomain(Configurator configurator, String id) {

        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);
        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();

        DomainMapping mapping = DomainMapping.getInstance();

        SearchResponse<Domain> response = repository.find(id, mapping);

        Domain domain = response.getSource();
        domain.setId(response.getId());

        repository.closeClient();
        return domain;
    }

    public String indexDomain(Domain document) {
        Configurator configurator = new Configurator();
        return indexDomain(configurator, document);
    }

    public String indexDomain(Configurator configurator, Domain domain) {
        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);

        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();

        DomainMapping mapping = DomainMapping.getInstance();
        DomainType document = mapping.getDocumentType(domain);

        String response = repository.indexMapping(mapping, document);
        repository.closeClient();

        return response;
    }

    public String updateDomain(Domain document) {
        Configurator configurator = new Configurator();
        return updateDomain(configurator, document);
    }

    public String updateDomain(Configurator configurator, Domain domain) {
        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);

        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();

        DomainMapping mapping = DomainMapping.getInstance();
        DomainType document = mapping.getDocumentType(domain);

        String response = repository.updateIndexMapping(domain.getId(), mapping, document);
        repository.closeClient();

        return response;
    }
}
