package com.qsocialnow.elasticsearch.services.cases;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.cases.CaseMapping;
import com.qsocialnow.elasticsearch.mappings.types.cases.CaseType;
import com.qsocialnow.elasticsearch.repositories.RangeFilter;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;
import com.qsocialnow.elasticsearch.repositories.ShouldConditionsFilter;
import com.qsocialnow.elasticsearch.repositories.ShouldFilter;
import com.qsocialnow.elasticsearch.repositories.TermFieldFilter;

import io.searchbox.core.SearchResult;

public class CaseTicketService extends CaseIndexService {

    private static final Logger log = LoggerFactory.getLogger(CaseTicketService.class);

    public CaseTicketService(AWSElasticsearchConfigurationProvider configurationProvider) {
        super(configurationProvider);
        initIndex();
    }

    public Case findCaseById(String originIdCase) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        mapping.setIndex(getQueryIndex());
        SearchResponse<Case> response = repository.findByAlias(originIdCase, mapping);

        Case caseDocument = response.getSource();
        repository.closeClient();
        return caseDocument;
    }

    public List<Case> getCases(int from, int size, String sortField, boolean sortOrder, String domainId,
            String triggerId, String segmentId, String subject, String title, String pendingResponse, String priority,
            String status, String fromOpenDate, String toOpenDate, List<String> teamsToFilter, String userName,
            String userSelected, String caseCategory, String subjectCategory) {

        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        log.info("retrieving cases from :" + from + " size" + size + " sorted by;" + sortField);

        RangeFilter rangeFilter = new RangeFilter("openDate", fromOpenDate, toOpenDate);
        List<RangeFilter> rangeFilters = new ArrayList<>();
        rangeFilters.add(rangeFilter);

        Map<String, String> searchValues = new HashMap<>();

        if (domainId != null)
            searchValues.put("domainId", domainId);

        if (triggerId != null)
            searchValues.put("triggerId", triggerId);

        if (segmentId != null)
            searchValues.put("segmentId", segmentId);

        if (title != null)
            searchValues.put("title", title);

        if (pendingResponse != null)
            searchValues.put("pendingResponse", pendingResponse);

        if (status != null)
            searchValues.put("open", status);

        if (priority != null)
            searchValues.put("priority", priority);

        if (userSelected != null)
            searchValues.put("assignee.username", userSelected);

        if (caseCategory != null)
            searchValues.put("caseCategories", caseCategory);

        if (subjectCategory != null)
            searchValues.put("subject.subjectCategory", subjectCategory);

        List<ShouldConditionsFilter> shouldConditionsFilters = new ArrayList<>();

        if (teamsToFilter == null || (teamsToFilter != null && teamsToFilter.size() == 0)) {
            if (userName != null) {
                searchValues.put("assignee.username", userName);
            }
        } else {
            ShouldConditionsFilter conditionFilterTeam = new ShouldConditionsFilter();
            for (String teamId : teamsToFilter) {
                ShouldFilter shouldFilter = new ShouldFilter("teamId", teamId);
                conditionFilterTeam.addShouldCondition(shouldFilter);
            }
            ShouldFilter shouldFilter = new ShouldFilter("assignee.username", userName);
            conditionFilterTeam.addShouldCondition(shouldFilter);
            shouldConditionsFilters.add(conditionFilterTeam);
        }

        if (subject != null) {
            ShouldConditionsFilter conditionFilterSubject = new ShouldConditionsFilter();
            ShouldFilter shouldFilterSubjetIdentifier = new ShouldFilter("subject.identifier", subject);
            ShouldFilter shouldFilterSubjetSourceName = new ShouldFilter("subject.sourceName", subject);
            conditionFilterSubject.addShouldCondition(shouldFilterSubjetIdentifier);
            conditionFilterSubject.addShouldCondition(shouldFilterSubjetSourceName);
            shouldConditionsFilters.add(conditionFilterSubject);
        }

        SearchResponse<Case> response = repository.queryByFields(mapping, from, size, sortField,
                Boolean.valueOf(sortOrder), searchValues, null, rangeFilters, shouldConditionsFilters);

        List<Case> cases = response.getSources();

        repository.closeClient();
        return cases;
    }

    public List<Case> getCasesByFilters(CasesFilterRequest filterRequest) {

        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();
        CaseMapping mapping = CaseMapping.getInstance();

        SearchResponse<Case> response;
        if (!filterRequest.isFilterActive() && filterRequest.getUserName() == null) {
            response = repository.queryMatchAll(filterRequest.getPageRequest().getOffset(), filterRequest
                    .getPageRequest().getPageSize(), filterRequest.getPageRequest().getSortField(), filterRequest
                    .getPageRequest().getSortOrder(), mapping);
        } else {
            String fromValue = null;
            if (filterRequest.getFromDate() != null) {
                fromValue = String.valueOf(filterRequest.getFromDate());
            }
            String toValue = null;
            if (filterRequest.getToDate() != null) {
                toValue = String.valueOf(filterRequest.getToDate());
            }

            List<RangeFilter> rangeFilters = new ArrayList<>();
            if (fromValue != null || toValue != null) {
                RangeFilter rangeFilter = new RangeFilter("openDate", fromValue, toValue);
                rangeFilters.add(rangeFilter);
            }

            Map<String, String> searchValues = new HashMap<>();
            if (filterRequest.getTitle() != null)
                searchValues.put("title", filterRequest.getTitle());

            List<TermFieldFilter> termFilters = new ArrayList<>();
            if (filterRequest.getDomain() != null)
                termFilters.add(new TermFieldFilter("domainId", filterRequest.getDomain()));

            if (filterRequest.getTrigger() != null)
                termFilters.add(new TermFieldFilter("triggerId", filterRequest.getTrigger()));

            if (filterRequest.getSegment() != null)
                termFilters.add(new TermFieldFilter("segmentId", filterRequest.getSegment()));

            if (filterRequest.getPendingResponse() != null)
                termFilters.add(new TermFieldFilter("pendingResponse", filterRequest.getPendingResponse()));

            if (filterRequest.getStatus() != null)
                termFilters.add(new TermFieldFilter("open", filterRequest.getStatus()));

            if (filterRequest.getPriority() != null)
                termFilters.add(new TermFieldFilter("priority", filterRequest.getPriority()));

            if (filterRequest.getUserSelected() != null)
                termFilters.add(new TermFieldFilter("assignee.username", filterRequest.getUserSelected()));

            if (filterRequest.getCaseCategory() != null)
                termFilters.add(new TermFieldFilter("caseCategories", filterRequest.getCaseCategory()));

            if (filterRequest.getSubjectCategory() != null)
                termFilters.add(new TermFieldFilter("subject.subjectCategory", filterRequest.getSubjectCategory()));

            List<ShouldConditionsFilter> shouldConditionsFilters = new ArrayList<>();
            List<String> teamsToFilter = filterRequest.getTeamsToFilter();
            if (teamsToFilter == null || (teamsToFilter != null && teamsToFilter.size() == 0)) {
                if (filterRequest.getUsername() != null) {
                    searchValues.put("assignee.username", filterRequest.getUsername());
                }
            } else {
                ShouldConditionsFilter conditionFilterTeam = new ShouldConditionsFilter();
                for (String teamId : teamsToFilter) {
                    ShouldFilter shouldFilter = new ShouldFilter("teamId", teamId);
                    conditionFilterTeam.addShouldCondition(shouldFilter);
                }
                if (filterRequest.getUsername() != null) {
                    ShouldFilter shouldFilter = new ShouldFilter("assignee.username", filterRequest.getUsername());
                    conditionFilterTeam.addShouldCondition(shouldFilter);
                }
                shouldConditionsFilters.add(conditionFilterTeam);
            }

            if (filterRequest.getSubject() != null) {
                ShouldConditionsFilter conditionFilterSubject = new ShouldConditionsFilter();
                ShouldFilter shouldFilterSubjetIdentifier = new ShouldFilter("subject.identifier",
                        filterRequest.getSubject());
                ShouldFilter shouldFilterSubjetSourceName = new ShouldFilter("subject.sourceName",
                        filterRequest.getSubject());
                conditionFilterSubject.addShouldCondition(shouldFilterSubjetIdentifier);
                conditionFilterSubject.addShouldCondition(shouldFilterSubjetSourceName);
                shouldConditionsFilters.add(conditionFilterSubject);
            }

            if (filterRequest.getMediums() != null && filterRequest.getMediums().length > 0) {
                if (filterRequest.getMediums().length > 1) {
                    ShouldConditionsFilter conditionFilterMedia = new ShouldConditionsFilter();
                    Integer[] medias = filterRequest.getMediums();
                    for (Integer media : medias) {
                        ShouldFilter shouldFilter = new ShouldFilter("triggerEvent.medioId", String.valueOf(media));
                        conditionFilterMedia.addShouldCondition(shouldFilter);
                    }
                    shouldConditionsFilters.add(conditionFilterMedia);
                } else {
                    termFilters.add(new TermFieldFilter("triggerEvent.medioId", String.valueOf(filterRequest
                            .getMediums()[0])));
                }
            }

            if (filterRequest.getLanguages() != null && filterRequest.getLanguages().length > 0) {
                if (filterRequest.getLanguages().length > 1) {
                    ShouldConditionsFilter conditionFilterLang = new ShouldConditionsFilter();
                    String[] languages = filterRequest.getLanguages();
                    for (String language : languages) {
                        ShouldFilter shouldFilter = new ShouldFilter("triggerEvent.language", language);
                        conditionFilterLang.addShouldCondition(shouldFilter);
                    }
                    shouldConditionsFilters.add(conditionFilterLang);
                } else {
                    termFilters.add(new TermFieldFilter("triggerEvent.language", filterRequest.getLanguages()[0]));
                }
            }
            response = repository.queryByFields(mapping, filterRequest.getPageRequest().getOffset(), filterRequest
                    .getPageRequest().getPageSize(), filterRequest.getPageRequest().getSortField(), filterRequest
                    .getPageRequest().getSortOrder(), searchValues, termFilters, rangeFilters, shouldConditionsFilters);
        }
        List<Case> cases = response.getSources();
        repository.closeClient();
        return cases;
    }

    public JsonObject getCasesAsJsonObject(int from, int size, String sortField, boolean sortOrder, String domainId,
            String triggerId, String segmentId, String subject, String title, String description,
            String pendingResponse, String status, String fromOpenDate, String toOpenDate, List<String> teamsToFilter,
            String userName, String userSelected) {

        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        log.info("retrieving cases from :" + from + " size" + size + " sorted by;" + sortField);

        RangeFilter rangeFilter = new RangeFilter("openDate", fromOpenDate, toOpenDate);
        List<RangeFilter> rangeFilters = new ArrayList<>();
        rangeFilters.add(rangeFilter);

        Map<String, String> searchValues = new HashMap<>();

        if (domainId != null)
            searchValues.put("domainId", domainId);

        if (triggerId != null)
            searchValues.put("triggerId", triggerId);

        if (segmentId != null)
            searchValues.put("segmentId", segmentId);

        if (subject != null)
            searchValues.put("subject.identifier", subject);

        if (title != null)
            searchValues.put("title", title);

        if (description != null)
            searchValues.put("description", description);

        if (pendingResponse != null)
            searchValues.put("pendingResponse", pendingResponse);

        if (status != null)
            searchValues.put("open", status);

        if (userSelected != null)
            searchValues.put("assignee.username", userSelected);

        List<ShouldFilter> shouldFilters = null;

        if (teamsToFilter == null || (teamsToFilter != null && teamsToFilter.size() == 0)) {
            if (userName != null) {
                searchValues.put("assignee.username", userName);
            }
        } else {
            shouldFilters = new ArrayList<>();
            for (String teamId : teamsToFilter) {
                ShouldFilter shouldFilter = new ShouldFilter("teamId", teamId);
                shouldFilters.add(shouldFilter);
            }
            ShouldFilter shouldFilter = new ShouldFilter("assignee.username", userName);
            shouldFilters.add(shouldFilter);
        }

        SearchResult response = repository.queryByFieldsAsJson(mapping, from, size, sortField,
                Boolean.valueOf(sortOrder), searchValues, rangeFilters, shouldFilters);

        JsonObject jsonObject = response.getJsonObject();
        repository.closeClient();
        return jsonObject;
    }

    public String indexCase(Case document) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();

        String indexName = this.getIndex(repository);
        mapping.setIndex(indexName);

        // index document
        document.setLastModifiedTimestamp(new Date().getTime());
        CaseType documentIndexed = mapping.getDocumentType(document);
        String response = repository.indexMapping(mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public String update(Case document) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();

        String indexName = this.getQueryIndex();
        mapping.setIndex(indexName);

        // searching to retrieve index value
        SearchResponse<Case> indexResponse = repository.findByAlias(document.getId(), mapping);
        mapping.setIndex(indexResponse.getIndex());

        // index document
        document.setLastModifiedTimestamp(new Date().getTime());
        CaseType documentIndexed = mapping.getDocumentType(document);
        String response = repository.updateMapping(document.getId(), mapping, documentIndexed);
        repository.closeClient();
        return response;
    }

    public Map<String, Long> getCasesCountByResolution(String domainId) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        log.info("retrieving cases from :" + domainId);

        Map<String, String> searchValues = new HashMap<>();
        if (domainId != null)
            searchValues.put("domainId", domainId);

        SearchResponse<Case> response = repository
                .queryByFieldsAndAggs(mapping, searchValues, null, null, "resolution");
        Map<String, Long> results = response.getCountAggregation();
        repository.closeClient();
        return results;
    }
}
