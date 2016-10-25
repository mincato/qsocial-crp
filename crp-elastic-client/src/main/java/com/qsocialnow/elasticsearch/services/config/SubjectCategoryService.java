package com.qsocialnow.elasticsearch.services.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.SubjectCategoryMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.SubjectCategoryType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class SubjectCategoryService {

    private static final Logger log = LoggerFactory.getLogger(SubjectCategoryService.class);

    private AWSElasticsearchConfigurationProvider configurator;

    private ConfigurationIndexService indexConfiguration;

    public void setConfigurator(AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

    public void setIndexConfiguration(ConfigurationIndexService indexConfiguration) {
        this.indexConfiguration = indexConfiguration;
    }

    public Map<String, String> findAllAsMap() {
        RepositoryFactory<SubjectCategoryType> esfactory = new RepositoryFactory<SubjectCategoryType>(configurator);
        Repository<SubjectCategoryType> repository = esfactory.initManager();
        repository.initClient();

        SubjectCategoryMapping mapping = SubjectCategoryMapping.getInstance(indexConfiguration.getIndexName());

        SearchResponse<SubjectCategory> response = repository.search(mapping);
        List<SubjectCategory> subjectCategories = response.getSources();

        repository.closeClient();
        Map<String, String> map = new HashMap<String, String>();
        if (!CollectionUtils.isEmpty(subjectCategories)) {
            for (SubjectCategory category : subjectCategories) {
                map.put(category.getId(), category.getDescription());
            }
        }

        return map;
    }

}
