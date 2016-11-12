package com.qsocialnow.elasticsearch.services.cases;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.CasesFilterRequest;
import com.qsocialnow.common.model.config.WordFilterType;
import com.qsocialnow.common.model.filter.FollowersCountRange;
import com.qsocialnow.common.model.filter.WordsListFilterBean;
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

    private static final String AUTHOR_REGEX = "{0} http.*|{0}";

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
            List<RangeFilter> rangeFilters = new ArrayList<>();
            Map<String, String> searchValues = new HashMap<>();
            List<TermFieldFilter> termFilters = new ArrayList<>();
            List<ShouldConditionsFilter> shouldConditionsFilters = new ArrayList<>();
            List<ShouldConditionsFilter> shouldTermsConditionsFilters = new ArrayList<>();
            List<ShouldConditionsFilter> shouldConditionsRegexpFilters = new ArrayList<>();
            configureFilters(filterRequest, searchValues, termFilters, rangeFilters, shouldConditionsFilters,
                    shouldTermsConditionsFilters, shouldConditionsRegexpFilters);
            response = repository.queryByFields(mapping, filterRequest.getPageRequest().getOffset(), filterRequest
                    .getPageRequest().getPageSize(), filterRequest.getPageRequest().getSortField(), filterRequest
                    .getPageRequest().getSortOrder(), searchValues, termFilters, rangeFilters, shouldConditionsFilters,
                    shouldTermsConditionsFilters, shouldConditionsRegexpFilters);
        }
        List<Case> cases = response.getSources();
        repository.closeClient();
        return cases;
    }

    private void configureFilters(CasesFilterRequest filterRequest, Map<String, String> searchValues,
            List<TermFieldFilter> termFilters, List<RangeFilter> rangeFilters,
            List<ShouldConditionsFilter> shouldConditionsFilters,
            List<ShouldConditionsFilter> shouldTermsConditionsFilters,
            List<ShouldConditionsFilter> shouldConditionsRegexpFilters) {
        String fromValue = null;
        if (filterRequest.getFromDate() != null) {
            fromValue = String.valueOf(filterRequest.getFromDate());
        }
        String toValue = null;
        if (filterRequest.getToDate() != null) {
            toValue = String.valueOf(filterRequest.getToDate());
        }

        if (fromValue != null || toValue != null) {
            RangeFilter rangeFilter = new RangeFilter("openDate", fromValue, toValue);
            rangeFilters.add(rangeFilter);
        }

        if (filterRequest.getTitle() != null)
            searchValues.put("title", filterRequest.getTitle());

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
                termFilters.add(new TermFieldFilter("triggerEvent.medioId",
                        String.valueOf(filterRequest.getMediums()[0])));
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

        if (filterRequest.getTimeFrom() != null) {
            if (filterRequest.getTimeFrom() != null || filterRequest.getTimeTo() != null) {
                RangeFilter rangeFilter = new RangeFilter("triggerEvent.fecha", String.valueOf(filterRequest
                        .getTimeFrom()), String.valueOf(filterRequest.getTimeTo()));
                rangeFilters.add(rangeFilter);
            }
        }

        // adding text terms filter
        if (filterRequest.getCloudsurfer() != null && filterRequest.getCloudsurfer().getWordList() != null) {
            WordsListFilterBean[] wordsList = filterRequest.getCloudsurfer().getWordList();
            // text
            List<WordsListFilterBean> textsList = getWordsByType(wordsList, WordFilterType.TEXT);
            if (textsList != null && !textsList.isEmpty()) {
                if (textsList.size() > 1) {
                    ShouldConditionsFilter conditionTermFilterText = new ShouldConditionsFilter();
                    for (WordsListFilterBean textWord : textsList) {
                        ShouldFilter shouldFilter = new ShouldFilter("triggerEvent.texto", textWord.getPalabra());
                        conditionTermFilterText.addShouldCondition(shouldFilter);
                    }
                    shouldTermsConditionsFilters.add(conditionTermFilterText);
                } else {
                    TermFieldFilter termFilter = new TermFieldFilter("triggerEvent.texto", textsList.get(0)
                            .getPalabra());
                    termFilter.setNeedSplit(true);
                    termFilters.add(termFilter);
                }
            }
            // authors
            List<WordsListFilterBean> authorsList = getWordsByType(wordsList, WordFilterType.AUTHOR);
            if (authorsList != null && !authorsList.isEmpty()) {
                for (WordsListFilterBean wordsListFilterBean : authorsList) {
                    String[] authors = wordsListFilterBean.getPalabra().split("\\,");
                    ShouldConditionsFilter authorsFilterLang = new ShouldConditionsFilter();
                    for (String author : authors) {
                        String regex = MessageFormat.format(AUTHOR_REGEX, author.trim().toLowerCase());
                        ShouldFilter shouldFilter = new ShouldFilter("triggerEvent.usuarioCreacion.raw", regex);
                        authorsFilterLang.addShouldCondition(shouldFilter);
                    }
                    for (String author : authors) {
                        String regex = MessageFormat.format(AUTHOR_REGEX, author.trim().toLowerCase());
                        ShouldFilter shouldFilter = new ShouldFilter("triggerEvent.usuarioReproduccion.raw", regex);
                        authorsFilterLang.addShouldCondition(shouldFilter);
                    }
                    shouldConditionsRegexpFilters.add(authorsFilterLang);
                }
            }

            // mentions
            List<WordsListFilterBean> mentionsList = getWordsByType(wordsList, WordFilterType.MENTION);
            if (mentionsList != null && !mentionsList.isEmpty()) {
                if (mentionsList.size() > 1) {
                    ShouldConditionsFilter hashTermsFilterText = new ShouldConditionsFilter();
                    for (WordsListFilterBean textWord : mentionsList) {
                        String mentions = textWord.getPalabra().replaceAll("@", "");
                        ShouldFilter shouldFilter = new ShouldFilter("triggerEvent.menciones", mentions);
                        hashTermsFilterText.addShouldCondition(shouldFilter);
                    }
                    shouldTermsConditionsFilters.add(hashTermsFilterText);
                } else {
                    String mentions = mentionsList.get(0).getPalabra().replaceAll("@", "");
                    TermFieldFilter termFilter = new TermFieldFilter("triggerEvent.menciones", mentions);
                    termFilter.setNeedSplit(true);
                    termFilters.add(termFilter);
                }
            }

            // hashtags
            List<WordsListFilterBean> hashTagsList = getWordsByType(wordsList, WordFilterType.HASHTAG);
            if (hashTagsList != null && !hashTagsList.isEmpty()) {
                if (hashTagsList.size() > 1) {
                    ShouldConditionsFilter hashTermsFilterText = new ShouldConditionsFilter();
                    for (WordsListFilterBean textWord : hashTagsList) {
                        String hashTags = textWord.getPalabra().replaceAll("#", "");
                        ShouldFilter shouldFilter = new ShouldFilter("triggerEvent.hashTags", hashTags);
                        hashTermsFilterText.addShouldCondition(shouldFilter);
                    }
                    shouldTermsConditionsFilters.add(hashTermsFilterText);
                } else {
                    String hashTags = hashTagsList.get(0).getPalabra().replaceAll("#", "");
                    TermFieldFilter termFilter = new TermFieldFilter("triggerEvent.hashTags", hashTags);
                    termFilter.setNeedSplit(true);
                    termFilters.add(termFilter);
                }

            }

        }

        if (filterRequest.getConnotations() != null && filterRequest.getConnotations().length > 0) {
            if (filterRequest.getConnotations().length > 1) {
                ShouldConditionsFilter conditionFilterCon = new ShouldConditionsFilter();
                String[] connotations = filterRequest.getConnotations();
                for (String connotation : connotations) {
                    ShouldFilter shouldFilter = new ShouldFilter("triggerEvent.connotacion", connotation);
                    conditionFilterCon.addShouldCondition(shouldFilter);
                }
                shouldTermsConditionsFilters.add(conditionFilterCon);
            } else {
                termFilters.add(new TermFieldFilter("triggerEvent.connotacion", filterRequest.getConnotations()[0]));
            }
        }
        // adding followers filter
        RangeFilter followerRange = getFollowersRange(filterRequest);
        if (followerRange != null) {
            rangeFilters.add(followerRange);
        }

        // adding categories filter
        if (filterRequest.getCategories() != null && filterRequest.getCategories().length > 0) {
            if (filterRequest.getCategories().length > 1) {
                ShouldConditionsFilter conditionFilterCategory = new ShouldConditionsFilter();
                Long[] categories = filterRequest.getCategories();
                for (Long category : categories) {
                    ShouldFilter shouldFilter = new ShouldFilter("triggerEvent.categorias", String.valueOf(category));
                    conditionFilterCategory.addShouldCondition(shouldFilter);
                }
                shouldTermsConditionsFilters.add(conditionFilterCategory);
            } else {
                termFilters.add(new TermFieldFilter("triggerEvent.categorias", String.valueOf(filterRequest
                        .getCategories()[0])));
            }
        }

    }

    private RangeFilter getFollowersRange(CasesFilterRequest filterRequest) {
        RangeFilter rangeFilter = null;
        if (filterRequest.getRange() != null && filterRequest.getRange().getFollowersCount() != null) {
            FollowersCountRange followerRange = filterRequest.getRange().getFollowersCount();

            if (followerRange.getGreaterThan() != null || followerRange.getLessThan() != null) {
                rangeFilter = new RangeFilter("triggerEvent.followersCount", String.valueOf(followerRange
                        .getGreaterThan()), String.valueOf(followerRange.getLessThan()));
            }
        }
        return rangeFilter;
    }

    private List<WordsListFilterBean> getWordsByType(WordsListFilterBean[] wordsList, WordFilterType type) {
        List<WordsListFilterBean> words = Arrays.asList(wordsList);
        List<WordsListFilterBean> resultTextWords = words.stream()
                .filter(word -> type.getName().equals(word.getTipo())).collect(Collectors.toList());
        return resultTextWords;
    }

    public JsonObject getCasesAsJsonObject(int from, int size, String sortField, boolean sortOrder,
            CasesFilterRequest filterRequest) {

        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();
        log.info("retrieving cases from :" + from + " size" + size + " sorted by;" + sortField);
        SearchResult response = null;
        if (!filterRequest.isFilterActive() && filterRequest.getUserName() == null) {
            response = repository.queryMatchAllAsJson(from, size, sortField, Boolean.valueOf(sortOrder), mapping);
        } else {
            List<RangeFilter> rangeFilters = new ArrayList<>();
            Map<String, String> searchValues = new HashMap<>();
            List<TermFieldFilter> termFilters = new ArrayList<>();
            List<ShouldConditionsFilter> shouldConditionsFilters = new ArrayList<>();
            List<ShouldConditionsFilter> shouldTermsConditionsFilters = new ArrayList<>();
            List<ShouldConditionsFilter> shouldConditionsRegexpFilters = new ArrayList<>();

            configureFilters(filterRequest, searchValues, termFilters, rangeFilters, shouldConditionsFilters,
                    shouldTermsConditionsFilters, shouldConditionsRegexpFilters);

            response = repository.queryByFieldsAsJson(mapping, from, size, sortField, Boolean.valueOf(sortOrder),
                    searchValues, termFilters, rangeFilters, shouldConditionsFilters, shouldTermsConditionsFilters,
                    shouldConditionsRegexpFilters);
        }
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

    public Map<String, Long> getCasesCountByResolution(CasesFilterRequest filterRequest) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();

        List<RangeFilter> rangeFilters = new ArrayList<>();
        Map<String, String> searchValues = new HashMap<>();
        List<TermFieldFilter> termFilters = new ArrayList<>();
        List<ShouldConditionsFilter> shouldConditionsFilters = new ArrayList<>();
        List<ShouldConditionsFilter> shouldTermsConditionsFilters = new ArrayList<>();
        List<ShouldConditionsFilter> shouldConditionsRegexpFilters = new ArrayList<>();

        configureFilters(filterRequest, searchValues, termFilters, rangeFilters, shouldConditionsFilters,
                shouldTermsConditionsFilters, shouldConditionsRegexpFilters);

        SearchResponse<Case> response = repository.queryByFieldsAndAggs(mapping, searchValues, rangeFilters,
                shouldConditionsFilters, termFilters, "resolution");
        Map<String, Long> results = response.getCountAggregation();
        repository.closeClient();
        return results;
    }

    public Map<String, Long> getResolutionsByAssigned(CasesFilterRequest filterRequest) {
        RepositoryFactory<CaseType> esfactory = new RepositoryFactory<CaseType>(elasticSearchCaseConfigurator);
        Repository<CaseType> repository = esfactory.initManager();
        repository.initClient();

        CaseMapping mapping = CaseMapping.getInstance();

        List<RangeFilter> rangeFilters = new ArrayList<>();
        Map<String, String> searchValues = new HashMap<>();
        List<TermFieldFilter> termFilters = new ArrayList<>();
        List<ShouldConditionsFilter> shouldConditionsFilters = new ArrayList<>();
        List<ShouldConditionsFilter> shouldTermsConditionsFilters = new ArrayList<>();
        List<ShouldConditionsFilter> shouldConditionsRegexpFilters = new ArrayList<>();

        configureFilters(filterRequest, searchValues, termFilters, rangeFilters, shouldConditionsFilters,
                shouldTermsConditionsFilters, shouldConditionsRegexpFilters);

        if (filterRequest.getIdResolution() != null)
            searchValues.put("resolution", filterRequest.getIdResolution());

        SearchResponse<Case> response = repository.queryByFieldsAndAggs(mapping, searchValues, rangeFilters,
                shouldConditionsFilters, termFilters, "assignee.username");
        Map<String, Long> results = response.getCountAggregation();
        repository.closeClient();
        return results;
    }

}
