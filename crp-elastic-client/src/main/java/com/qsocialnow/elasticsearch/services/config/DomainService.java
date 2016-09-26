package com.qsocialnow.elasticsearch.services.config;

import java.util.List;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.DomainMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.DomainType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class DomainService {

    private TriggerService triggerService;

    private ResolutionService resolutionService;

    private AWSElasticsearchConfigurationProvider configurator;

    public Domain findDomain(String id) {

        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);
        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();

        DomainMapping mapping = DomainMapping.getInstance();

        SearchResponse<Domain> response = repository.find(id, mapping);

        Domain domain = response.getSource();

        repository.closeClient();
        return domain;
    }

    public Domain findDomainWithResolutions(String id) {
        Domain domain = findDomain(id);
        domain.setResolutions(resolutionService.getResolutions(id));
        return domain;
    }

    public String indexDomain(Domain domain) {
        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);

        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();

        DomainMapping mapping = DomainMapping.getInstance();
        DomainType document = mapping.getDocumentType(domain);

        String response = repository.indexMapping(mapping, document);
        repository.closeClient();

        resolutionService.indexResolutions(response, domain.getResolutions());

        return response;
    }

    public String updateDomain(Domain domain) {
        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);

        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();

        DomainMapping mapping = DomainMapping.getInstance();
        DomainType document = mapping.getDocumentType(domain);

        String response = repository.updateMapping(domain.getId(), mapping, document);
        repository.closeClient();

        resolutionService.updateResolutions(domain.getId(), domain.getResolutions());

        return response;
    }

    public List<Domain> getDomains(Integer offset, Integer limit) {
        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);
        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();

        DomainMapping mapping = DomainMapping.getInstance();
        SearchResponse<Domain> response = repository.queryMatchAll(offset, limit, "name", mapping);

        List<Domain> domains = response.getSources();

        repository.closeClient();
        return domains;
    }

    public List<Domain> getDomainsByName(Integer offset, Integer limit, String name) {
        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);
        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();
        DomainMapping mapping = DomainMapping.getInstance();
        SearchResponse<Domain> response = repository.queryByField(mapping, offset, limit, "name", "name", name);
        List<Domain> domains = response.getSources();
        repository.closeClient();
        return domains;
    }

    public Domain findDomainWithTriggers(String domainId) {
        Domain domain = findDomain(domainId);
        if (domain != null) {
            domain.setTriggers(triggerService.getTriggers(domainId));
        }
        return domain;
    }

    public void setTriggerService(TriggerService triggerService) {
        this.triggerService = triggerService;
    }

    public void setResolutionService(ResolutionService resolutionService) {
        this.resolutionService = resolutionService;
    }

    public void setConfigurator(AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

}
