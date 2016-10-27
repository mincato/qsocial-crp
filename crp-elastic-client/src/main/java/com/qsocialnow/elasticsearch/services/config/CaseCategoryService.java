package com.qsocialnow.elasticsearch.services.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.CaseCategoryMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.CaseCategoryType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class CaseCategoryService {

    private AWSElasticsearchConfigurationProvider configurator;

    private ConfigurationIndexService indexConfiguration;

    public void setConfigurator(AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

    public void setIndexConfiguration(ConfigurationIndexService indexConfiguration) {
        this.indexConfiguration = indexConfiguration;
    }

    public Map<String, String> findAllAsMap() {
        RepositoryFactory<CaseCategoryType> esfactory = new RepositoryFactory<CaseCategoryType>(configurator);
        Repository<CaseCategoryType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategoryMapping mapping = CaseCategoryMapping.getInstance(indexConfiguration.getIndexName());

        SearchResponse<CaseCategory> response = repository.search(mapping);
        List<CaseCategory> caseCategories = response.getSources();

        repository.closeClient();
        Map<String, String> map = new HashMap<String, String>();
        if (!CollectionUtils.isEmpty(caseCategories)) {
            for (CaseCategory category : caseCategories) {
                map.put(category.getId(), category.getDescription());
            }
        }

        return map;
    }

}
