package com.qsocialnow.elasticsearch.services.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.mappings.config.DomainMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.DomainType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class DomainService {

    private TriggerService triggerService;

    private ResolutionService resolutionService;

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

        repository.closeClient();
        return domain;
    }

    public Domain findDomainWithResolutions(Configurator configurator, String id) {
        Domain domain = findDomain(configurator, id);
        domain.setResolutions(resolutionService.getResolutions(configurator, id));
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

        indexResolutions(configurator, response, domain.getResolutions());

        return response;
    }

    private void indexResolutions(Configurator configurator, String domainId, List<Resolution> resolutions) {
        if (CollectionUtils.isNotEmpty(resolutions)) {
            for (Resolution resolution : resolutions) {
                resolutionService.indexResolution(configurator, domainId, resolution);
            }
        }
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

        updateResolutions(configurator, domain.getId(), domain.getResolutions());

        return response;
    }

    private void updateResolutions(Configurator configurator, String domainId, List<Resolution> newResolutions) {
        List<Resolution> oldResolutions = resolutionService.getResolutions(configurator, domainId);
        Set<String> oldResolutionsIds = oldResolutions.stream().map(Resolution::getId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(newResolutions)) {
            for (Resolution resolution : newResolutions) {
                if (resolution.getId() != null) {
                    oldResolutionsIds.remove(resolution.getId());
                    resolutionService.updateResolution(configurator, domainId, resolution);
                } else {
                    resolutionService.indexResolution(configurator, domainId, resolution);
                }
            }
        }
        for (String resolutionsToRemove : oldResolutionsIds) {
            resolutionService.deleteResolution(configurator, domainId, resolutionsToRemove);
        }

    }

    public List<Domain> getDomains(Configurator configurator, Integer offset, Integer limit) {
        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);
        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();

        DomainMapping mapping = DomainMapping.getInstance();
        SearchResponse<Domain> response = repository.search(offset, limit, "name", mapping);

        List<Domain> domains = response.getSources();

        repository.closeClient();
        return domains;
    }

    public List<Domain> getDomainsByName(Configurator configurator, Integer offset, Integer limit, String name) {
        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);
        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();

        DomainMapping mapping = DomainMapping.getInstance();
        SearchResponse<Domain> response = repository.search(offset, limit, "name", name, mapping);

        List<Domain> domains = response.getSources();

        repository.closeClient();
        return domains;
    }

    public Domain findDomainWithTriggers(Configurator configurator, String domainId) {
        Domain domain = findDomain(domainId);
        domain.setTriggers(triggerService.getTriggers(configurator, domainId));
        return domain;
    }

    public void setTriggerService(TriggerService triggerService) {
        this.triggerService = triggerService;
    }

    public void setResolutionService(ResolutionService resolutionService) {
        this.resolutionService = resolutionService;
    }
}
