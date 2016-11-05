package com.qsocialnow.elasticsearch.services.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;

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

    private AWSElasticsearchConfigurationProvider configurator;

    private ConfigurationIndexService indexConfiguration;

    public CaseCategorySet indexCaseCategorySet(CaseCategorySet caseCategorySet, List<CaseCategory> caseCategories) {
        RepositoryFactory<CaseCategorySetType> esfactory = new RepositoryFactory<CaseCategorySetType>(configurator);

        Repository<CaseCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategorySetMapping mapping = CaseCategorySetMapping.getInstance(indexConfiguration.getIndexName());

        // index document
        CaseCategorySetType documentIndexed = mapping.getDocumentType(caseCategorySet);
        String idCaseCategorySet = repository.indexMappingAndRefresh(mapping, documentIndexed);

        caseCategorySet.setId(idCaseCategorySet);
        repository.closeClient();

        RepositoryFactory<CaseCategoryType> esCaseCategoryfactory = new RepositoryFactory<CaseCategoryType>(
                configurator);
        List<CaseCategory> indexedCaseCategories = new ArrayList<>();

        Repository<CaseCategoryType> repositoryCaseCategory = esCaseCategoryfactory.initManager();
        repositoryCaseCategory.initClient();

        CaseCategoryMapping mappingCaseCategory = CaseCategoryMapping.getInstance(indexConfiguration.getIndexName());

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

        CaseCategoryMapping mapping = CaseCategoryMapping.getInstance(indexConfiguration.getIndexName());

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

        CaseCategorySetMapping mapping = CaseCategorySetMapping.getInstance(indexConfiguration.getIndexName());
        BoolQueryBuilder filters = null;
        if (name != null) {
            filters = QueryBuilders.boolQuery();
            filters = filters.must(QueryBuilders.matchQuery("description", name));
        }

        SearchResponse<CaseCategorySet> response = repository.searchWithFilters(offset, limit, "description",
                SortOrder.ASC, filters, mapping);
        List<CaseCategorySet> caseCategorySets = response.getSources();

        repository.closeClient();
        return caseCategorySets;
    }

    public List<CaseCategorySet> findAllActive() {

        RepositoryFactory<CaseCategorySetType> esfactory = new RepositoryFactory<CaseCategorySetType>(configurator);
        Repository<CaseCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategorySetMapping mapping = CaseCategorySetMapping.getInstance(indexConfiguration.getIndexName());

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.matchQuery("active", true));

        SearchResponse<CaseCategorySet> response = repository.searchWithFilters(filters, mapping);
        List<CaseCategorySet> caseCategorySets = response.getSources();

        repository.closeClient();
        return caseCategorySets;
    }

    public CaseCategorySet findOne(String caseCategorySetId) {
        RepositoryFactory<CaseCategorySetType> esfactory = new RepositoryFactory<CaseCategorySetType>(configurator);
        Repository<CaseCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategorySetMapping mapping = CaseCategorySetMapping.getInstance(indexConfiguration.getIndexName());

        SearchResponse<CaseCategorySet> response = repository.find(caseCategorySetId, mapping);

        CaseCategorySet caseCategorySet = response.getSource();
        repository.closeClient();

        RepositoryFactory<CaseCategoryType> esCaseCategoryfactory = new RepositoryFactory<CaseCategoryType>(
                configurator);

        Repository<CaseCategoryType> repositoryCaseCategory = esCaseCategoryfactory.initManager();
        repositoryCaseCategory.initClient();
        CaseCategoryMapping caseCategoryMapping = CaseCategoryMapping.getInstance(indexConfiguration.getIndexName());

        SearchResponse<CaseCategory> responseCaseCategories = repositoryCaseCategory.queryByField(caseCategoryMapping,
                0, -1, "description", "idCaseCategorySet", caseCategorySetId);

        List<CaseCategory> categories = responseCaseCategories.getSources();
        caseCategorySet.setCategories(categories);

        repositoryCaseCategory.closeClient();
        return caseCategorySet;
    }

    public CaseCategorySet findOneWithActiveCategories(String caseCategorySetId) {
        RepositoryFactory<CaseCategorySetType> esfactory = new RepositoryFactory<CaseCategorySetType>(configurator);
        Repository<CaseCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategorySetMapping mapping = CaseCategorySetMapping.getInstance(indexConfiguration.getIndexName());

        SearchResponse<CaseCategorySet> response = repository.find(caseCategorySetId, mapping);

        CaseCategorySet caseCategorySet = response.getSource();
        repository.closeClient();

        RepositoryFactory<CaseCategoryType> esCaseCategoryfactory = new RepositoryFactory<CaseCategoryType>(
                configurator);

        Repository<CaseCategoryType> repositoryCaseCategory = esCaseCategoryfactory.initManager();
        repositoryCaseCategory.initClient();
        CaseCategoryMapping caseCategoryMapping = CaseCategoryMapping.getInstance(indexConfiguration.getIndexName());

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.matchQuery("active", true));
        filters = filters.must(QueryBuilders.matchQuery("idCaseCategorySet", caseCategorySetId));

        SearchResponse<CaseCategory> responseCaseCategories = repositoryCaseCategory.searchWithFilters("description",
                SortOrder.ASC, filters, caseCategoryMapping);

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
        CaseCategoryMapping caseCategoryMapping = CaseCategoryMapping.getInstance(indexConfiguration.getIndexName());

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

        CaseCategorySetMapping mapping = CaseCategorySetMapping.getInstance(indexConfiguration.getIndexName());
        CaseCategorySetType documentIndexed = mapping.getDocumentType(caseCategorySet);
        String response = repository.updateMapping(caseCategorySet.getId(), mapping, documentIndexed);
        repository.closeClient();

        updateCategories(caseCategorySet.getId(), toUpdateCategories);

        return response;
    }

    private void updateCategories(String setId, List<CaseCategory> categoriesToUpdate) {

        RepositoryFactory<CaseCategoryType> esCaseCategoryfactory = new RepositoryFactory<CaseCategoryType>(
                configurator);
        Repository<CaseCategoryType> repositoryCaseCategory = esCaseCategoryfactory.initManager();
        repositoryCaseCategory.initClient();

        CaseCategoryMapping mappingCaseCategory = CaseCategoryMapping.getInstance(indexConfiguration.getIndexName());
        SearchResponse<CaseCategory> responseCaseCategories = repositoryCaseCategory.queryByField(mappingCaseCategory,
                0, -1, "description", "idCaseCategorySet", setId);

        List<CaseCategory> categoriesFromRepo = responseCaseCategories.getSources();

        List<CaseCategory> categories = activateAllCategoriesToUpdate(categoriesToUpdate);

        categories.addAll(deactivateAllRemovedCategories(categoriesToUpdate, categoriesFromRepo));

        categories.stream().forEach(cat -> {
            CaseCategoryType documentCaseCategoryIndexed = mappingCaseCategory.getDocumentType(cat);
            documentCaseCategoryIndexed.setIdCaseCategorySet(setId);
            repositoryCaseCategory.updateMapping(cat.getId(), mappingCaseCategory, documentCaseCategoryIndexed);
        });

        repositoryCaseCategory.closeClient();
    }

    private List<CaseCategory> activateAllCategoriesToUpdate(List<CaseCategory> categoriesToUpdate) {
        List<CaseCategory> categories = categoriesToUpdate.stream().map(updCat -> {
            updCat.setActive(true);
            return updCat;
        }).collect(Collectors.toList());
        return categories;
    }

    private List<CaseCategory> deactivateAllRemovedCategories(List<CaseCategory> categoriesToUpdate,
            List<CaseCategory> categoriesFromRepo) {
        return categoriesFromRepo.stream().map(repoCat -> {
            repoCat.setActive(categoryIsPresentInList(repoCat, categoriesToUpdate));
            return repoCat;
        }).filter(repoCat -> !repoCat.getActive()).collect(Collectors.toList());
    }

    private boolean categoryIsPresentInList(CaseCategory category, List<CaseCategory> list) {
        return list.stream().filter(c -> c.getId() != null && c.getId().equals(category.getId())).findFirst()
                .isPresent();
    }

    public List<CaseCategorySet> findByIds(List<String> ids) {
        RepositoryFactory<CaseCategorySetType> esfactory = new RepositoryFactory<CaseCategorySetType>(configurator);
        Repository<CaseCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategorySetMapping mapping = CaseCategorySetMapping.getInstance(indexConfiguration.getIndexName());

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.idsQuery(mapping.getType()).ids(ids));

        SearchResponse<CaseCategorySet> response = repository.searchWithFilters(filters, mapping);

        List<CaseCategorySet> caseCategorySets = response.getSources();

        repository.closeClient();
        return caseCategorySets;
    }

    public List<CaseCategorySet> findActiveByIds(List<String> ids) {
        RepositoryFactory<CaseCategorySetType> esfactory = new RepositoryFactory<CaseCategorySetType>(configurator);
        Repository<CaseCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategorySetMapping mapping = CaseCategorySetMapping.getInstance(indexConfiguration.getIndexName());

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.idsQuery(mapping.getType()).ids(ids));
        filters = filters.must(QueryBuilders.matchQuery("active", true));

        SearchResponse<CaseCategorySet> response = repository.searchWithFilters(filters, mapping);

        List<CaseCategorySet> caseCategorySets = response.getSources();

        repository.closeClient();
        return caseCategorySets;
    }

    public void setIndexConfiguration(ConfigurationIndexService indexConfiguration) {
        this.indexConfiguration = indexConfiguration;
    }

    public List<CaseCategory> findCategories() {
        RepositoryFactory<CaseCategoryType> esfactory = new RepositoryFactory<CaseCategoryType>(configurator);
        Repository<CaseCategoryType> repository = esfactory.initManager();
        repository.initClient();

        CaseCategoryMapping mapping = CaseCategoryMapping.getInstance(indexConfiguration.getIndexName());
        SearchResponse<CaseCategory> response = repository.searchWithFilters(null, null, "description", SortOrder.ASC,
                null, mapping);
        List<CaseCategory> caseCategories = response.getSources();

        repository.closeClient();
        return caseCategories;
    }

}
