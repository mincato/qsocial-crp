package com.qsocialnow.elasticsearch.services.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.ResolutionMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.ResolutionType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class ResolutionService {

    private AWSElasticsearchConfigurationProvider configurator;

    private ConfigurationIndexService indexConfiguration;

    public String indexResolution(String domainId, Resolution resolution) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(configurator);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        String response = indexResolution(domainId, resolution, repository);
        repository.closeClient();
        return response;
    }

    private String indexResolution(String domainId, Resolution resolution, Repository<ResolutionType> repository) {
        ResolutionMapping mapping = ResolutionMapping.getInstance(indexConfiguration.getIndexName());
        mapping.setIdParent(domainId);

        // index document
        ResolutionType documentIndexed = mapping.getDocumentType(resolution);
        String response = repository.indexChildMapping(mapping, documentIndexed);
        return response;
    }

    public String updateResolution(String domainId, Resolution resolution) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(configurator);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        String response = updateResolution(domainId, resolution, repository);
        repository.closeClient();

        return response;
    }

    private String updateResolution(String domainId, Resolution resolution, Repository<ResolutionType> repository) {
        ResolutionMapping mapping = ResolutionMapping.getInstance(indexConfiguration.getIndexName());
        mapping.setIdParent(domainId);

        ResolutionType documentIndexed = mapping.getDocumentType(resolution);

        String response = repository.updateChildMapping(resolution.getId(), mapping, documentIndexed);
        return response;
    }

    private void deactivateResolution(String domainId, Resolution resolution, Repository<ResolutionType> repository) {
        ResolutionMapping mapping = ResolutionMapping.getInstance(indexConfiguration.getIndexName());
        mapping.setIdParent(domainId);

        resolution.setActive(false);
        ResolutionType documentIndexed = mapping.getDocumentType(resolution);

        repository.updateChildMapping(resolution.getId(), mapping, documentIndexed);
    }

    public void deleteResolution(String domainId, String resolutionId) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(configurator);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        deleteResolution(domainId, resolutionId, repository);
        repository.closeClient();
    }

    private void deleteResolution(String domainId, String resolutionId, Repository<ResolutionType> repository) {
        ResolutionMapping mapping = ResolutionMapping.getInstance(indexConfiguration.getIndexName());
        mapping.setIdParent(domainId);

        repository.removeChildMapping(resolutionId, mapping);
    }

    public List<Resolution> getResolutions(String domainId) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(configurator);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        List<Resolution> resolutions = getResolutions(domainId, repository);

        repository.closeClient();
        return resolutions;
    }

    public List<Resolution> getActiveResolutions(String domainId) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(configurator);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        List<Resolution> resolutions = getActiveResolutions(domainId, repository);

        repository.closeClient();
        return resolutions;
    }

    private List<Resolution> getResolutions(String domainId, Repository<ResolutionType> repository) {
        ResolutionMapping mapping = ResolutionMapping.getInstance(indexConfiguration.getIndexName());
        mapping.setIdParent(domainId);

        SearchResponse<Resolution> response = repository.searchChildMapping(mapping);

        List<Resolution> resolutions = response.getSources();
        return resolutions;
    }

    public Resolution findResolution(String domainId, String resolutionId) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(configurator);
        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();
        ResolutionMapping mapping = ResolutionMapping.getInstance(indexConfiguration.getIndexName());
        mapping.setIdParent(domainId);
        QueryBuilder filters = QueryBuilders.matchQuery("_id", resolutionId);
        SearchResponse<Resolution> response = repository.searchChildMappingWithFilters(filters, mapping);

        List<Resolution> resolutions = response.getSources();
        if (!CollectionUtils.isEmpty(resolutions)) {
            return resolutions.get(0);
        }
        return null;
    }

    private List<Resolution> getActiveResolutions(String domainId, Repository<ResolutionType> repository) {
        ResolutionMapping mapping = ResolutionMapping.getInstance(indexConfiguration.getIndexName());
        mapping.setIdParent(domainId);

        QueryBuilder filters = QueryBuilders.matchQuery("active", true);
        SearchResponse<Resolution> response = repository.searchChildMappingWithFilters(filters, mapping);

        List<Resolution> resolutions = response.getSources();
        return resolutions;
    }

    public void indexResolutions(String domainId, List<Resolution> resolutions) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(configurator);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        if (CollectionUtils.isNotEmpty(resolutions)) {
            for (Resolution resolution : resolutions) {
                indexResolution(domainId, resolution, repository);
            }
        }
        repository.closeClient();

    }

    public void updateResolutions(String domainId, List<Resolution> newResolutions) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(configurator);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        List<Resolution> oldResolutions = getResolutions(domainId, repository);
        Set<String> oldResolutionsIds = oldResolutions.stream().map(Resolution::getId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(newResolutions)) {
            for (Resolution resolution : newResolutions) {
                if (resolution.getId() != null) {
                    oldResolutionsIds.remove(resolution.getId());
                    updateResolution(domainId, resolution, repository);
                } else {
                    indexResolution(domainId, resolution, repository);
                }
            }
        }
        for (String resolutionToRemove : oldResolutionsIds) {
            Resolution resolution = oldResolutions.stream().filter(res -> res.getId().equals(resolutionToRemove))
                    .findFirst().get();
            deactivateResolution(domainId, resolution, repository);
        }
        repository.closeClient();

    }

    public Map<String, String> getAllResolutionsAsMap() {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(configurator);
        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        ResolutionMapping mapping = ResolutionMapping.getInstance(indexConfiguration.getIndexName());
        SearchResponse<Resolution> response = repository.search(mapping);

        List<Resolution> resolutions = response.getSources();

        repository.closeClient();
        Map<String, String> map = new HashMap<String, String>();
        for (Resolution resolution : resolutions) {
            map.put(resolution.getId(), resolution.getDescription());
        }

        return map;
    }

    public void setConfigurator(AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

    public void setIndexConfiguration(ConfigurationIndexService indexConfiguration) {
        this.indexConfiguration = indexConfiguration;
    }

}
