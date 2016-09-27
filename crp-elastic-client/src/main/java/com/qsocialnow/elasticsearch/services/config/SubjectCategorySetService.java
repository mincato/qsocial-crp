package com.qsocialnow.elasticsearch.services.config;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.SubjectCategoryMapping;
import com.qsocialnow.elasticsearch.mappings.config.SubjectCategorySetMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.SubjectCategorySetType;
import com.qsocialnow.elasticsearch.mappings.types.config.SubjectCategoryType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class SubjectCategorySetService {

    private static final Logger log = LoggerFactory.getLogger(SubjectCategorySetService.class);

    private AWSElasticsearchConfigurationProvider configurator;

    public SubjectCategorySet indexSubjectCategorySet(SubjectCategorySet subjectCategorySet,
            List<SubjectCategory> subjectCategories) {
        RepositoryFactory<SubjectCategorySetType> esfactory = new RepositoryFactory<SubjectCategorySetType>(
                configurator);

        Repository<SubjectCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        SubjectCategorySetMapping mapping = SubjectCategorySetMapping.getInstance();

        // index document
        SubjectCategorySetType documentIndexed = mapping.getDocumentType(subjectCategorySet);
        String idSubjectCategorySet = repository.indexMapping(mapping, documentIndexed);

        subjectCategorySet.setId(idSubjectCategorySet);
        repository.closeClient();

        RepositoryFactory<SubjectCategoryType> esSubjectCategoryfactory = new RepositoryFactory<SubjectCategoryType>(
                configurator);
        List<SubjectCategory> indexedSubjectCategories = new ArrayList<>();

        Repository<SubjectCategoryType> repositorySubjectCategory = esSubjectCategoryfactory.initManager();
        repositorySubjectCategory.initClient();

        SubjectCategoryMapping mappingCaseCategory = SubjectCategoryMapping.getInstance();

        for (SubjectCategory subjectCategory : subjectCategories) {
            SubjectCategoryType documentSubjectCategoryIndexed = mappingCaseCategory.getDocumentType(subjectCategory);
            documentSubjectCategoryIndexed.setIdSubjectCategorySet(idSubjectCategorySet);

            String response = repositorySubjectCategory.indexMapping(mappingCaseCategory,
                    documentSubjectCategoryIndexed);
            subjectCategory.setId(response);
            indexedSubjectCategories.add(subjectCategory);
        }
        subjectCategorySet.setCategories(indexedSubjectCategories);
        repositorySubjectCategory.closeClient();
        return subjectCategorySet;
    }

    public List<SubjectCategory> indexCaseCategories(String idSubjectCategorySet,
            List<SubjectCategory> subjectCategories) {
        RepositoryFactory<SubjectCategoryType> esfactory = new RepositoryFactory<SubjectCategoryType>(configurator);

        List<SubjectCategory> indexedSubjectCategories = new ArrayList<>();
        Repository<SubjectCategoryType> repository = esfactory.initManager();
        repository.initClient();

        SubjectCategoryMapping mapping = SubjectCategoryMapping.getInstance();

        for (SubjectCategory subjectCategory : subjectCategories) {
            SubjectCategoryType documentIndexed = mapping.getDocumentType(subjectCategory);
            documentIndexed.setIdSubjectCategorySet(idSubjectCategorySet);
            String response = repository.indexMapping(mapping, documentIndexed);
            subjectCategory.setId(response);
            indexedSubjectCategories.add(subjectCategory);
        }
        repository.closeClient();
        return indexedSubjectCategories;
    }

    public List<SubjectCategorySet> findAll(Integer offset, Integer limit, String name) {

        RepositoryFactory<SubjectCategorySetType> esfactory = new RepositoryFactory<SubjectCategorySetType>(
                configurator);
        Repository<SubjectCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        SubjectCategorySetMapping mapping = SubjectCategorySetMapping.getInstance();
        BoolQueryBuilder filters = null;
        if (name != null) {
            filters = QueryBuilders.boolQuery();
            filters = filters.must(QueryBuilders.matchQuery("description", name));
        }

        SearchResponse<SubjectCategorySet> response = repository.searchWithFilters(offset, limit, "description",
                filters, mapping);
        List<SubjectCategorySet> subjectCategorySets = response.getSources();

        repository.closeClient();
        return subjectCategorySets;
    }

    public SubjectCategorySet findOne(String subjectCategorySetId) {
        RepositoryFactory<SubjectCategorySetType> esfactory = new RepositoryFactory<SubjectCategorySetType>(
                configurator);
        Repository<SubjectCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        SubjectCategorySetMapping mapping = SubjectCategorySetMapping.getInstance();

        SearchResponse<SubjectCategorySet> response = repository.find(subjectCategorySetId, mapping);

        SubjectCategorySet subjectCategorySet = response.getSource();
        repository.closeClient();

        RepositoryFactory<SubjectCategoryType> esSubjectCategoryfactory = new RepositoryFactory<SubjectCategoryType>(
                configurator);

        Repository<SubjectCategoryType> repositorySubjectCategory = esSubjectCategoryfactory.initManager();
        repositorySubjectCategory.initClient();
        SubjectCategoryMapping subjectCategoryMapping = SubjectCategoryMapping.getInstance();

        SearchResponse<SubjectCategory> responseSubjectCategories = repositorySubjectCategory.queryByField(
                subjectCategoryMapping, 0, -1, "description", "idSubjectCategorySet", subjectCategorySetId);

        List<SubjectCategory> categories = responseSubjectCategories.getSources();
        subjectCategorySet.setCategories(categories);

        repositorySubjectCategory.closeClient();
        return subjectCategorySet;
    }

    public void setConfigurator(AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

    public String updateSubjectCategorySet(SubjectCategorySet subjectCategorySet) {

        List<SubjectCategory> toUpdateCategories = subjectCategorySet.getCategories();

        RepositoryFactory<SubjectCategorySetType> esfactory = new RepositoryFactory<SubjectCategorySetType>(
                configurator);
        Repository<SubjectCategorySetType> repository = esfactory.initManager();
        repository.initClient();

        SubjectCategorySetMapping mapping = SubjectCategorySetMapping.getInstance();
        SubjectCategorySetType documentIndexed = mapping.getDocumentType(subjectCategorySet);
        String response = repository.updateMapping(subjectCategorySet.getId(), mapping, documentIndexed);
        repository.closeClient();

        // subject categories
        RepositoryFactory<SubjectCategoryType> esSubjectCategoryfactory = new RepositoryFactory<SubjectCategoryType>(
                configurator);
        Repository<SubjectCategoryType> repositorySubjectCategory = esSubjectCategoryfactory.initManager();
        repositorySubjectCategory.initClient();

        SubjectCategoryMapping mappingSubjectCategory = SubjectCategoryMapping.getInstance();
        SearchResponse<SubjectCategory> responseCaseCategories = repositorySubjectCategory.queryByField(
                mappingSubjectCategory, 0, -1, "description", "idSubjectCategorySet", subjectCategorySet.getId());

        List<SubjectCategory> categories = responseCaseCategories.getSources();

        for (SubjectCategory subjectCategory : categories) {
            boolean isUpdated = false;
            for (SubjectCategory toUpdatedSubjectCategory : toUpdateCategories) {
                if (subjectCategory.getId().equals(toUpdatedSubjectCategory.getId())) {
                    isUpdated = true;
                    break;
                }
            }
            if (!isUpdated) {
                repositorySubjectCategory.removeMapping(subjectCategory.getId(), mappingSubjectCategory);
                log.info("SubjectCategory from ES needs to be removed:" + subjectCategory.getId());
            }
        }

        for (SubjectCategory subjectCategory : toUpdateCategories) {
            SubjectCategoryType documentSubjectCategoryIndexed = mappingSubjectCategory
                    .getDocumentType(subjectCategory);
            documentSubjectCategoryIndexed.setIdSubjectCategorySet(subjectCategorySet.getId());
            repositorySubjectCategory.updateMapping(subjectCategory.getId(), mappingSubjectCategory,
                    documentSubjectCategoryIndexed);
        }
        repositorySubjectCategory.initClient();
        return response;
    }

}
