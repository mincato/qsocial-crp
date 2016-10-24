package com.qsocialnow.elasticsearch.services.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.DomainMapMapping;
import com.qsocialnow.elasticsearch.mappings.config.DomainMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.DomainType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class DomainService {

    private TriggerService triggerService;

    private ResolutionService resolutionService;

    private AWSElasticsearchConfigurationProvider configurator;

    private ConfigurationIndexService indexConfiguration;

    public Domain findDomain(String id) {

        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);
        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();

        DomainMapping mapping = DomainMapping.getInstance(indexConfiguration.getIndexName());

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

        DomainMapping mapping = DomainMapping.getInstance(indexConfiguration.getIndexName());
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

        DomainMapping mapping = DomainMapping.getInstance(indexConfiguration.getIndexName());
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

        DomainMapping mapping = DomainMapping.getInstance(indexConfiguration.getIndexName());
        SearchResponse<Domain> response = repository.searchWithFilters(offset, limit, "name", null, mapping);

        List<Domain> domains = response.getSources();

        repository.closeClient();
        return domains;
    }

    public Map<String, String> getAllDomainsAsMap() {
        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);
        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();

        DomainMapMapping mapping = DomainMapMapping.getInstance(indexConfiguration.getIndexName());
        SearchResponse<Domain> response = repository.search(mapping);

        List<Domain> domains = response.getSources();

        repository.closeClient();
        Map<String, String> map = new HashMap<String, String>();
        for (Domain domain : domains) {
            map.put(domain.getId(), domain.getName());
        }

        return map;
    }

    public List<Domain> getDomainsByName(Integer offset, Integer limit, String name) {
        RepositoryFactory<DomainType> esfactory = new RepositoryFactory<DomainType>(configurator);
        Repository<DomainType> repository = esfactory.initManager();
        repository.initClient();
        DomainMapping mapping = DomainMapping.getInstance(indexConfiguration.getIndexName());

        BoolQueryBuilder filters = null;
        if (name != null) {
            filters = QueryBuilders.boolQuery();
            filters = filters.must(QueryBuilders.matchQuery("name", name));
        }

        SearchResponse<Domain> response = repository.searchWithFilters(offset, limit, "name", filters, mapping);
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

    public Domain findDomainWithActiveTriggers(String domainId) {
        Domain domain = findDomain(domainId);
        if (domain != null) {
            domain.setTriggers(triggerService.getActiveTriggers(domainId));
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

    public void setIndexConfiguration(ConfigurationIndexService indexConfiguration) {
        this.indexConfiguration = indexConfiguration;
    }

}
