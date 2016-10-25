package com.qsocialnow.elasticsearch.services.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.Status;
import com.qsocialnow.common.model.config.Trigger;
import com.qsocialnow.common.model.config.TriggerReport;
import com.qsocialnow.common.model.request.TriggerListRequest;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.TriggerMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.TriggerType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class TriggerService {

    private AWSElasticsearchConfigurationProvider configurator;

    private ConfigurationIndexService indexConfiguration;

    private SegmentService segmentService;

    public String indexTrigger(String domainId, Trigger trigger) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);

        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance(indexConfiguration.getIndexName());

        // index document
        TriggerType documentIndexed = mapping.getDocumentType(trigger);
        documentIndexed.setDomainId(domainId);
        String response = repository.indexMapping(mapping, documentIndexed);
        repository.closeClient();

        segmentService.indexSegments(response, trigger.getSegments());

        return response;
    }

    public List<Trigger> getTriggers(String domainId, Integer offset, Integer limit,
            TriggerListRequest triggerListRequest) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);
        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance(indexConfiguration.getIndexName());

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.matchQuery("domainId", domainId));
        if (triggerListRequest.getName() != null) {
            filters = filters.must(QueryBuilders.matchQuery("name", triggerListRequest.getName()));
        }
        if (triggerListRequest.getStatus() != null) {
            filters = filters.must(QueryBuilders.matchQuery("status", triggerListRequest.getStatus()));
        }
        if (triggerListRequest.getFromDate() != null) {
            filters = filters.filter(QueryBuilders.rangeQuery("init").lte(
                    Long.parseLong(triggerListRequest.getFromDate())));
        }
        if (triggerListRequest.getToDate() != null) {
            filters = filters.filter(QueryBuilders.rangeQuery("end")
                    .gte(Long.parseLong(triggerListRequest.getToDate())));
        }
        SearchResponse<Trigger> response = repository.searchWithFilters(offset, limit, "name", filters, mapping);
        List<Trigger> triggers = response.getSources();

        repository.closeClient();
        return triggers;
    }

    public List<Trigger> getTriggers(String domainId) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);
        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance(indexConfiguration.getIndexName());

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.matchQuery("domainId", domainId));
        SearchResponse<Trigger> response = repository.searchWithFilters(filters, mapping);

        List<Trigger> triggers = response.getSources();

        repository.closeClient();
        for (Trigger trigger : triggers) {
            trigger.setSegments(segmentService.getSegments(trigger.getId()));
        }
        return triggers;

    }

    public List<Trigger> getActiveTriggers(String domainId) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);
        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance(indexConfiguration.getIndexName());

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.matchQuery("domainId", domainId));
        filters = filters.must(QueryBuilders.matchQuery("status", Status.ACTIVE));
        SearchResponse<Trigger> response = repository.searchWithFilters(filters, mapping);

        List<Trigger> triggers = response.getSources();

        repository.closeClient();
        for (Trigger trigger : triggers) {
            trigger.setSegments(segmentService.getSegments(trigger.getId()));
        }
        return triggers;

    }

    public Trigger findOne(String triggerId) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);
        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance(indexConfiguration.getIndexName());

        SearchResponse<Trigger> response = repository.find(triggerId, mapping);

        Trigger triggers = response.getSource();

        repository.closeClient();
        return triggers;
    }

    public Trigger findTriggerWithSegments(String triggerId) {
        Trigger trigger = findOne(triggerId);
        if (trigger != null) {
            trigger.setSegments(segmentService.getSegments(triggerId));
        }
        return trigger;
    }

    public void setConfigurator(AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

    public String updateTrigger(String domainId, Trigger trigger) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);

        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance(indexConfiguration.getIndexName());

        // index document
        TriggerType documentIndexed = mapping.getDocumentType(trigger);
        documentIndexed.setDomainId(domainId);
        String response = repository.updateMapping(trigger.getId(), mapping, documentIndexed);
        repository.closeClient();

        segmentService.updateSegments(trigger.getId(), trigger.getSegments());

        return response;
    }

    public List<Trigger> getTriggersByIds(List<String> triggerIds) {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);
        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();
        TriggerMapping mapping = TriggerMapping.getInstance(indexConfiguration.getIndexName());
        SearchResponse<Trigger> response = repository.queryByIds(mapping, null, triggerIds);
        List<Trigger> triggers = response.getSources();
        repository.closeClient();
        return triggers;
    }

    public void setSegmentService(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    public void setIndexConfiguration(ConfigurationIndexService indexConfiguration) {
        this.indexConfiguration = indexConfiguration;
    }

    public Map<String, TriggerReport> getAllTriggersAsMap() {
        RepositoryFactory<TriggerType> esfactory = new RepositoryFactory<TriggerType>(configurator);
        Repository<TriggerType> repository = esfactory.initManager();
        repository.initClient();

        TriggerMapping mapping = TriggerMapping.getInstance(indexConfiguration.getIndexName());

        SearchResponse<Trigger> response = repository.search(mapping);

        List<Trigger> triggers = response.getSources();

        repository.closeClient();
        Map<String, TriggerReport> map = new HashMap<String, TriggerReport>();
        for (Trigger trigger : triggers) {
            TriggerReport triggerReport = new TriggerReport();
            triggerReport.setName(trigger.getName());
            List<Segment> segments = segmentService.getSegments(trigger.getId());
            for (Segment segment : segments) {
                triggerReport.getSegments().put(segment.getId(), segment.getDescription());
            }
            map.put(trigger.getId(), triggerReport);
        }
        return map;
    }

}
