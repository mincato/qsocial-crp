package com.qsocialnow.elasticsearch.services.cases;

import java.util.List;

import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.cases.SubjectMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.SubjectType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class SubjectService extends DynamicIndexService {

    private static AWSElasticsearchConfigurationProvider elasticSearchCaseConfigurator;

    public SubjectService(AWSElasticsearchConfigurationProvider configurationProvider) {
        elasticSearchCaseConfigurator = configurationProvider;
    }

    public String indexSubject(Subject document) {

        RepositoryFactory<SubjectType> esfactory = new RepositoryFactory<SubjectType>(
                elasticSearchCaseConfigurator);

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

    public List<Subject> findSubjects(int from, int size) {

        RepositoryFactory<SubjectType> esfactory = new RepositoryFactory<SubjectType>(
                elasticSearchCaseConfigurator);
        Repository<SubjectType> repository = esfactory.initManager();
        repository.initClient();

        SubjectMapping mapping = SubjectMapping.getInstance();
        mapping.setIndex(this.getQueryIndex());

        
        SearchResponse<Subject> response = repository
                .queryMatchAll(from, size, "date",mapping);

        List<Subject> subjects = response.getSources();

        repository.closeClient();
        return subjects;
    }
    
    public Subject findSubjectById(String subjectId) {
    	 RepositoryFactory<SubjectType> esfactory = new RepositoryFactory<SubjectType>(elasticSearchCaseConfigurator);
         Repository<SubjectType> repository = esfactory.initManager();
         repository.initClient();

         SubjectMapping mapping = SubjectMapping.getInstance();

         String indexName = this.getQueryIndex();
         mapping.setIndex(indexName);

         // index document
         SearchResponse<Subject> searchResponse = repository.find(subjectId, mapping);
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
        String response = repository.updateMapping(document.getId(), mapping, documentIndexed);
        repository.closeClient();
        return response;
    }
    
}
