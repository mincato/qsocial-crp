package com.qsocialnow.elasticsearch.services.config;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.CaseCategoryMapping;
import com.qsocialnow.elasticsearch.mappings.config.CaseCategorySetMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.CaseCategorySetType;
import com.qsocialnow.elasticsearch.mappings.types.config.CaseCategoryType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class CaseCategorySetService {

    private static final Logger log = LoggerFactory.getLogger(CaseCategorySetService.class);

    private AWSElasticsearchConfigurationProvider configurator;

    public CaseCategorySet indexCaseCategorySet(CaseCategorySet caseCategorySet, List<CaseCategory> caseCategories) {
        RepositoryFactory<CaseCategorySetType> esfactory = new RepositoryFactory<CaseCategorySetType>(configurator);

        Repository<CaseCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategorySetMapping mapping = CaseCategorySetMapping.getInstance();

        // index document
        CaseCategorySetType documentIndexed = mapping.getDocumentType(caseCategorySet);
        String idCaseCategorySet = repository.indexMapping(mapping, documentIndexed);

        caseCategorySet.setId(idCaseCategorySet);
        repository.closeClient();

        RepositoryFactory<CaseCategoryType> esCaseCategoryfactory = new RepositoryFactory<CaseCategoryType>(
                configurator);
        List<CaseCategory> indexedCaseCategories = new ArrayList<>();

        Repository<CaseCategoryType> repositoryCaseCategory = esCaseCategoryfactory.initManager();
        repositoryCaseCategory.initClient();

        CaseCategoryMapping mappingCaseCategory = CaseCategoryMapping.getInstance();

        for (CaseCategory caseCategory : caseCategories) {
            CaseCategoryType documentCaseCategoryIndexed = mappingCaseCategory.getDocumentType(caseCategory);
            documentCaseCategoryIndexed.setIdCaseCategorySet(idCaseCategorySet);

            String response = repositoryCaseCategory.indexMapping(mappingCaseCategory, documentCaseCategoryIndexed);
            caseCategory.setId(response);
            indexedCaseCategories.add(caseCategory);
        }
        caseCategorySet.setCategories(indexedCaseCategories);
        repositoryCaseCategory.closeClient();
        return caseCategorySet;
    }

    public List<CaseCategory> indexCaseCategories(String idCaseCategorySet, List<CaseCategory> caseCategories) {
        RepositoryFactory<CaseCategoryType> esfactory = new RepositoryFactory<CaseCategoryType>(configurator);

        List<CaseCategory> indexedCaseCategories = new ArrayList<>();
        Repository<CaseCategoryType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategoryMapping mapping = CaseCategoryMapping.getInstance();

        for (CaseCategory caseCategory : caseCategories) {
            CaseCategoryType documentIndexed = mapping.getDocumentType(caseCategory);
            documentIndexed.setIdCaseCategorySet(idCaseCategorySet);
            String response = repository.indexMapping(mapping, documentIndexed);
            caseCategory.setId(response);
            indexedCaseCategories.add(caseCategory);
        }
        repository.closeClient();
        return indexedCaseCategories;
    }

    public List<CaseCategorySet> findAll(Integer offset, Integer limit, String name) {

        RepositoryFactory<CaseCategorySetType> esfactory = new RepositoryFactory<CaseCategorySetType>(configurator);
        Repository<CaseCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategorySetMapping mapping = CaseCategorySetMapping.getInstance();
        BoolQueryBuilder filters = null;
        if (name != null) {
            filters = QueryBuilders.boolQuery();
            filters = filters.must(QueryBuilders.matchQuery("description", name));
        }

        SearchResponse<CaseCategorySet> response = repository.searchWithFilters(offset, limit, "description", filters,
                mapping);
        List<CaseCategorySet> caseCategorySets = response.getSources();

        repository.closeClient();
        return caseCategorySets;
    }

    public CaseCategorySet findOne(String caseCategorySetId) {
        RepositoryFactory<CaseCategorySetType> esfactory = new RepositoryFactory<CaseCategorySetType>(configurator);
        Repository<CaseCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategorySetMapping mapping = CaseCategorySetMapping.getInstance();

        SearchResponse<CaseCategorySet> response = repository.find(caseCategorySetId, mapping);

        CaseCategorySet caseCategorySet = response.getSource();
        repository.closeClient();

        RepositoryFactory<CaseCategoryType> esCaseCategoryfactory = new RepositoryFactory<CaseCategoryType>(
                configurator);

        Repository<CaseCategoryType> repositoryCaseCategory = esCaseCategoryfactory.initManager();
        repositoryCaseCategory.initClient();
        CaseCategoryMapping caseCategoryMapping = CaseCategoryMapping.getInstance();

        SearchResponse<CaseCategory> responseCaseCategories = repositoryCaseCategory.queryByField(caseCategoryMapping,
                0, -1, "description", "idCaseCategorySet", caseCategorySetId);

        List<CaseCategory> categories = responseCaseCategories.getSources();
        caseCategorySet.setCategories(categories);

        repositoryCaseCategory.closeClient();
        return caseCategorySet;
    }

    public List<CaseCategory> findCategories(String caseCategorySetId) {
        RepositoryFactory<CaseCategoryType> esCaseCategoryfactory = new RepositoryFactory<CaseCategoryType>(
                configurator);

        Repository<CaseCategoryType> repositoryCaseCategory = esCaseCategoryfactory.initManager();
        repositoryCaseCategory.initClient();
        CaseCategoryMapping caseCategoryMapping = CaseCategoryMapping.getInstance();

        SearchResponse<CaseCategory> responseCaseCategories = repositoryCaseCategory.queryByField(caseCategoryMapping,
                0, -1, "description", "idCaseCategorySet", caseCategorySetId);

        List<CaseCategory> categories = responseCaseCategories.getSources();

        repositoryCaseCategory.closeClient();
        return categories;
    }

    public void setConfigurator(AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

    public String updateCaseCategorySet(CaseCategorySet caseCategorySet) {

        List<CaseCategory> toUpdateCategories = caseCategorySet.getCategories();

        RepositoryFactory<CaseCategorySetType> esfactory = new RepositoryFactory<CaseCategorySetType>(configurator);
        Repository<CaseCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategorySetMapping mapping = CaseCategorySetMapping.getInstance();
        CaseCategorySetType documentIndexed = mapping.getDocumentType(caseCategorySet);
        String response = repository.updateMapping(caseCategorySet.getId(), mapping, documentIndexed);
        repository.closeClient();

        // case categories
        RepositoryFactory<CaseCategoryType> esCaseCategoryfactory = new RepositoryFactory<CaseCategoryType>(
                configurator);
        Repository<CaseCategoryType> repositoryCaseCategory = esCaseCategoryfactory.initManager();
        repositoryCaseCategory.initClient();

        CaseCategoryMapping mappingCaseCategory = CaseCategoryMapping.getInstance();
        SearchResponse<CaseCategory> responseCaseCategories = repositoryCaseCategory.queryByField(mappingCaseCategory,
                0, -1, "description", "idCaseCategorySet", caseCategorySet.getId());

        List<CaseCategory> categories = responseCaseCategories.getSources();

        for (CaseCategory caseCategory : categories) {
            boolean isUpdated = false;
            for (CaseCategory toUpdatedCaseCategory : toUpdateCategories) {
                if (caseCategory.getId().equals(toUpdatedCaseCategory.getId())) {
                    isUpdated = true;
                    break;
                }
            }
            if (!isUpdated) {
                repositoryCaseCategory.removeMapping(caseCategory.getId(), mappingCaseCategory);
                log.info("CaseCategory from ES needs to be removed:" + caseCategory.getId());
            }
        }

        for (CaseCategory caseCategory : toUpdateCategories) {
            CaseCategoryType documentCaseCategoryIndexed = mappingCaseCategory.getDocumentType(caseCategory);
            documentCaseCategoryIndexed.setIdCaseCategorySet(caseCategorySet.getId());
            repositoryCaseCategory
                    .updateMapping(caseCategory.getId(), mappingCaseCategory, documentCaseCategoryIndexed);
        }
        repositoryCaseCategory.closeClient();
        return response;
    }

    public List<CaseCategorySet> findByIds(List<String> ids) {
        RepositoryFactory<CaseCategorySetType> esfactory = new RepositoryFactory<CaseCategorySetType>(configurator);
        Repository<CaseCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategorySetMapping mapping = CaseCategorySetMapping.getInstance();

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.idsQuery(mapping.getType()).ids(ids));

        SearchResponse<CaseCategorySet> response = repository.searchWithFilters(filters, mapping);

        List<CaseCategorySet> caseCategorySets = response.getSources();

        repository.closeClient();
        return caseCategorySets;
    }

}
