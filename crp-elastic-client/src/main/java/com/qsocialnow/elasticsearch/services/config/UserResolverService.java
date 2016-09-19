package com.qsocialnow.elasticsearch.services.config;

import java.util.List;

import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.UserResolverMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.UserResolverType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class UserResolverService {

    private AWSElasticsearchConfigurationProvider configurator;

    public String indexUserResolver(UserResolver userResolver) {
        RepositoryFactory<UserResolverType> esfactory = new RepositoryFactory<UserResolverType>(configurator);

        Repository<UserResolverType> repository = esfactory.initManager();
        repository.initClient();

        UserResolverMapping mapping = UserResolverMapping.getInstance();

        // validete index name
        boolean isCreated = repository.validateIndex(mapping.getIndex());
        // create index
        if (!isCreated) {
            repository.createIndex(mapping.getIndex());
        }

        // index document
        UserResolverType documentIndexed = mapping.getDocumentType(userResolver);
        String response = repository.indexMapping(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public List<UserResolver> getUserResolvers(Integer offset, Integer limit) {
        RepositoryFactory<UserResolverType> esfactory = new RepositoryFactory<UserResolverType>(configurator);
        Repository<UserResolverType> repository = esfactory.initManager();
        repository.initClient();

        UserResolverMapping mapping = UserResolverMapping.getInstance();

        SearchResponse<UserResolver> response = repository.search(offset, limit, "identifier", mapping);
        List<UserResolver> userResolvers = response.getSources();

        repository.closeClient();
        return userResolvers;
    }

    public UserResolver findOne(String userResolverId) {
        RepositoryFactory<UserResolverType> esfactory = new RepositoryFactory<UserResolverType>(configurator);
        Repository<UserResolverType> repository = esfactory.initManager();
        repository.initClient();

        UserResolverMapping mapping = UserResolverMapping.getInstance();

        SearchResponse<UserResolver> response = repository.find(userResolverId, mapping);

        UserResolver userResolver = response.getSource();

        repository.closeClient();
        return userResolver;
    }

    public void setConfigurator(AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

    public void deleteUserResolver(String userResolverId) {
        RepositoryFactory<UserResolverType> esfactory = new RepositoryFactory<UserResolverType>(configurator);

        Repository<UserResolverType> repository = esfactory.initManager();
        repository.initClient();

        UserResolverMapping mapping = UserResolverMapping.getInstance();

        repository.removeMapping(userResolverId, mapping);
        repository.closeClient();
    }

    public String updateUserResolver(UserResolver userResolver) {
        RepositoryFactory<UserResolverType> esfactory = new RepositoryFactory<UserResolverType>(configurator);

        Repository<UserResolverType> repository = esfactory.initManager();
        repository.initClient();

        UserResolverMapping mapping = UserResolverMapping.getInstance();
        UserResolverType documentIndexed = mapping.getDocumentType(userResolver);
        String response = repository.updateIndexMapping(userResolver.getId(), mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

}
