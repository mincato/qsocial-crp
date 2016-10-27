package com.qsocialnow.elasticsearch.services.cases;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.request.SubjectListRequest;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.cases.SubjectMapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.SubjectType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class SubjectService extends CaseIndexService {

    private static final Logger log = LoggerFactory.getLogger(SubjectService.class);

    public SubjectService(AWSElasticsearchConfigurationProvider configurationProvider) {
        super(configurationProvider);
        initIndex();
    }

    public String indexSubject(Subject document) {

        RepositoryFactory<SubjectType> esfactory = new RepositoryFactory<SubjectType>(elasticSearchCaseConfigurator);

        Repository<SubjectType> repository = esfactory.initManager();
        repository.initClient();

        SubjectMapping mapping = SubjectMapping.getInstance();
        mapping.setIndex(this.getIndex(repository));

        // index document
        SubjectType documentIndexed = mapping.getDocumentType(document);

        String response = repository.indexMappingAndRefresh(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public List<Subject> findSubjects(int from, int size, SubjectListRequest subjectListRequest) {

        RepositoryFactory<SubjectType> esfactory = new RepositoryFactory<SubjectType>(elasticSearchCaseConfigurator);
        Repository<SubjectType> repository = esfactory.initManager();
        repository.initClient();

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        if (subjectListRequest.getIdentifier() != null) {
            filters = filters.must(QueryBuilders.matchQuery("identifier", subjectListRequest.getIdentifier()));
        }
        if (subjectListRequest.getSource() != null) {
            filters = filters.must(QueryBuilders.matchQuery("source", subjectListRequest.getSource()));
        }

        SubjectMapping mapping = SubjectMapping.getInstance();
        mapping.setIndex(this.getQueryIndex());

        SearchResponse<Subject> response = repository.searchWithFilters(from, size, "identifier", SortOrder.ASC,
                filters, mapping);

        List<Subject> subjects = response.getSources();

        repository.closeClient();
        return subjects;
    }

    public Subject findSubjectsByOriginUser(String sourceId) {
        Subject subject = null;
        RepositoryFactory<SubjectType> esfactory = new RepositoryFactory<SubjectType>(elasticSearchCaseConfigurator);
        Repository<SubjectType> repository = esfactory.initManager();
        repository.initClient();

        SubjectMapping mapping = SubjectMapping.getInstance();
        mapping.setIndex(this.getQueryIndex());

        SearchResponse<Subject> response = repository.queryByField(mapping, 0, 0, null, "sourceId", sourceId);
        List<Subject> subjects = response.getSources();

        if (subjects != null && subjects.size() > 0) {
            log.info("Retrieving subject : " + sourceId);
            subject = subjects.get(0);
        }

        repository.closeClient();
        return subject;
    }

    public Subject findSubjectById(String subjectId) {
        RepositoryFactory<SubjectType> esfactory = new RepositoryFactory<SubjectType>(elasticSearchCaseConfigurator);
        Repository<SubjectType> repository = esfactory.initManager();
        repository.initClient();

        SubjectMapping mapping = SubjectMapping.getInstance();

        String indexName = this.getQueryIndex();
        mapping.setIndex(indexName);

        // index document
        SearchResponse<Subject> searchResponse = repository.findByAlias(subjectId, mapping);
        Subject response = searchResponse.getSource();

        repository.closeClient();
        return response;
    }

    public String update(Subject document) {
        RepositoryFactory<SubjectType> esfactory = new RepositoryFactory<SubjectType>(elasticSearchCaseConfigurator);
        Repository<SubjectType> repository = esfactory.initManager();
        repository.initClient();

        SubjectMapping mapping = SubjectMapping.getInstance();

        String indexName = this.getQueryIndex();
        mapping.setIndex(indexName);

        // searching to retrieve index value
        SearchResponse<Subject> indexResponse = repository.findByAlias(document.getId(), mapping);
        mapping.setIndex(indexResponse.getIndex());

        // index document
        SubjectType documentIndexed = mapping.getDocumentType(document);
        log.info("Updating subject from index: " + indexResponse.getIndex() + " id: " + document.getId());
        String response = repository.updateMapping(document.getId(), mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

}
