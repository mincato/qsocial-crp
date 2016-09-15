package com.qsocialnow.elasticsearch.services.config;

import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.UserResolverMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.UserResolverType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class UserResolverService {

    private AWSElasticsearchConfigurationProvider configurator;

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

}
