package com.qsocialnow.elasticsearch.services.config;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.qsocialnow.common.model.config.UserResolver;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.UserResolverMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.UserResolverType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class UserResolverService {

    private AWSElasticsearchConfigurationProvider configurator;

    private ConfigurationIndexService indexConfiguration;

    public String indexUserResolver(UserResolver userResolver) {
        RepositoryFactory<UserResolverType> esfactory = new RepositoryFactory<UserResolverType>(configurator);

        Repository<UserResolverType> repository = esfactory.initManager();
        repository.initClient();

        UserResolverMapping mapping = UserResolverMapping.getInstance(indexConfiguration.getIndexName());

        // index document
        UserResolverType documentIndexed = mapping.getDocumentType(userResolver);
        String response = repository.indexMapping(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public List<UserResolver> getUserResolvers(Integer offset, Integer limit, String identifier) {
        RepositoryFactory<UserResolverType> esfactory = new RepositoryFactory<UserResolverType>(configurator);
        Repository<UserResolverType> repository = esfactory.initManager();
        repository.initClient();

        BoolQueryBuilder filters = null;
        if (identifier != null) {
            filters = QueryBuilders.boolQuery();
            filters = filters.must(QueryBuilders.matchQuery("identifier", identifier));
        }

        UserResolverMapping mapping = UserResolverMapping.getInstance(indexConfiguration.getIndexName());

        SearchResponse<UserResolver> response = repository.searchWithFilters(offset, limit, null, filters, mapping);
        List<UserResolver> userResolvers = response.getSources();

        repository.closeClient();
        return userResolvers;
    }

    public UserResolver findOne(String userResolverId) {
        RepositoryFactory<UserResolverType> esfactory = new RepositoryFactory<UserResolverType>(configurator);
        Repository<UserResolverType> repository = esfactory.initManager();
        repository.initClient();

        UserResolverMapping mapping = UserResolverMapping.getInstance(indexConfiguration.getIndexName());

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

        UserResolverMapping mapping = UserResolverMapping.getInstance(indexConfiguration.getIndexName());

        repository.removeMapping(userResolverId, mapping);
        repository.closeClient();
    }

    public String updateUserResolver(UserResolver userResolver) {
        RepositoryFactory<UserResolverType> esfactory = new RepositoryFactory<UserResolverType>(configurator);

        Repository<UserResolverType> repository = esfactory.initManager();
        repository.initClient();

        UserResolverMapping mapping = UserResolverMapping.getInstance(indexConfiguration.getIndexName());
        UserResolverType documentIndexed = mapping.getDocumentType(userResolver);
        String response = repository.updateMapping(userResolver.getId(), mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public List<UserResolver> findByIds(List<String> ids, Boolean status, Long source) {
        RepositoryFactory<UserResolverType> esfactory = new RepositoryFactory<UserResolverType>(configurator);
        Repository<UserResolverType> repository = esfactory.initManager();
        repository.initClient();

        UserResolverMapping mapping = UserResolverMapping.getInstance(indexConfiguration.getIndexName());

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.idsQuery(mapping.getType()).ids(ids));
        if (status != null) {
            filters = filters.must(QueryBuilders.matchQuery("active", status));
        }
        if (source != null) {
            filters = filters.must(QueryBuilders.matchQuery("source", source));
        }

        SearchResponse<UserResolver> response = repository.searchWithFilters(filters, mapping);

        List<UserResolver> userResolvers = response.getSources();

        repository.closeClient();
        return userResolvers;
    }

    public void setIndexConfiguration(ConfigurationIndexService indexConfiguration) {
        this.indexConfiguration = indexConfiguration;
    }

    public List<UserResolver> getAllUserResolvers() {
        RepositoryFactory<UserResolverType> esfactory = new RepositoryFactory<UserResolverType>(configurator);
        Repository<UserResolverType> repository = esfactory.initManager();
        repository.initClient();

        UserResolverMapping mapping = UserResolverMapping.getInstance(indexConfiguration.getIndexName());

        SearchResponse<UserResolver> response = repository.search(mapping);
        List<UserResolver> userResolvers = response.getSources();

        repository.closeClient();
        return userResolvers;
    }

}
