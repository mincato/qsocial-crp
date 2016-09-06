package com.qsocialnow.elasticsearch.services.config;

import java.util.List;

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

        ResolutionMapping mapping = ResolutionMapping.getInstance();
        mapping.setIdParent(domainId);

        // validete index name
        boolean isCreated = repository.validateIndex(mapping.getIndex());
        // create index
        if (!isCreated) {
            repository.createIndex(mapping.getIndex());
        }

        // index document
        ResolutionType documentIndexed = mapping.getDocumentType(resolution);
        String response = repository.indexChildMapping(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public String updateResolution(Configurator elasticConfig, String domainId, Resolution resolution) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(elasticConfig);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        ResolutionMapping mapping = ResolutionMapping.getInstance();
        mapping.setIdParent(domainId);

        ResolutionType documentIndexed = mapping.getDocumentType(resolution);

        String response = repository.updateChildMapping(resolution.getId(), mapping, documentIndexed);
        repository.closeClient();

        return response;
    }

    public void deleteResolution(Configurator elasticConfig, String domainId, String resolutionId) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(elasticConfig);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        ResolutionMapping mapping = ResolutionMapping.getInstance();
        mapping.setIdParent(domainId);

        repository.removeChildMapping(resolutionId, mapping);
        repository.closeClient();
    }

    public List<Resolution> getResolutions(Configurator elasticConfig, String domainId) {
        RepositoryFactory<ResolutionType> esfactory = new RepositoryFactory<ResolutionType>(elasticConfig);

        Repository<ResolutionType> repository = esfactory.initManager();
        repository.initClient();

        ResolutionMapping mapping = ResolutionMapping.getInstance();
        mapping.setIdParent(domainId);

        SearchResponse<Resolution> response = repository.searchChildMapping(mapping);

        List<Resolution> resolutions = response.getSources();

        repository.closeClient();
        return resolutions;

    }

}
