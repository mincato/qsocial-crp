package com.qsocialnow.elasticsearch.services.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.elasticsearch.configuration.Configurator;
import com.qsocialnow.elasticsearch.mappings.config.ResolutionMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.ResolutionType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class ResolutionService {

    public String indexResolution(Configurator elasticConfig, String domainId, Resolution resolution) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(elasticConfig);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        String response = indexResolution(domainId, resolution, repository);
        repository.closeClient();
        return response;
    }

    private String indexResolution(String domainId, Resolution resolution, Repository<ResolutionType> repository) {
        ResolutionMapping mapping = ResolutionMapping.getInstance();
        mapping.setIdParent(domainId);

        // index document
        ResolutionType documentIndexed = mapping.getDocumentType(resolution);
        String response = repository.indexChildMapping(mapping, documentIndexed);
        return response;
    }

    public String updateResolution(Configurator elasticConfig, String domainId, Resolution resolution) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(elasticConfig);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        String response = updateResolution(domainId, resolution, repository);
        repository.closeClient();

        return response;
    }

    private String updateResolution(String domainId, Resolution resolution, Repository<ResolutionType> repository) {
        ResolutionMapping mapping = ResolutionMapping.getInstance();
        mapping.setIdParent(domainId);

        ResolutionType documentIndexed = mapping.getDocumentType(resolution);

        String response = repository.updateChildMapping(resolution.getId(), mapping, documentIndexed);
        return response;
    }

    public void deleteResolution(Configurator elasticConfig, String domainId, String resolutionId) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(elasticConfig);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        deleteResolution(domainId, resolutionId, repository);
        repository.closeClient();
    }

    private void deleteResolution(String domainId, String resolutionId, Repository<ResolutionType> repository) {
        ResolutionMapping mapping = ResolutionMapping.getInstance();
        mapping.setIdParent(domainId);

        repository.removeChildMapping(resolutionId, mapping);
    }

    public List<Resolution> getResolutions(Configurator elasticConfig, String domainId) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(elasticConfig);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        List<Resolution> resolutions = getResolutions(domainId, repository);

        repository.closeClient();
        return resolutions;

    }

    private List<Resolution> getResolutions(String domainId, Repository<ResolutionType> repository) {
        ResolutionMapping mapping = ResolutionMapping.getInstance();
        mapping.setIdParent(domainId);

        SearchResponse<Resolution> response = repository.searchChildMapping(mapping);

        List<Resolution> resolutions = response.getSources();
        return resolutions;
    }

    public void indexResolutions(Configurator configurator, String domainId, List<Resolution> resolutions) {
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

    public void updateResolutions(Configurator configurator, String domainId, List<Resolution> newResolutions) {
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
        for (String resolutionsToRemove : oldResolutionsIds) {
            deleteResolution(domainId, resolutionsToRemove, repository);
        }
        repository.closeClient();

    }

}
