package com.qsocialnow.elasticsearch.services.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.elasticsearch.configuration.AWSElasticsearchConfigurationProvider;
import com.qsocialnow.elasticsearch.mappings.config.SegmentMapping;
import com.qsocialnow.elasticsearch.mappings.types.config.SegmentType;
import com.qsocialnow.elasticsearch.repositories.Repository;
import com.qsocialnow.elasticsearch.repositories.RepositoryFactory;
import com.qsocialnow.elasticsearch.repositories.SearchResponse;

public class SegmentService {

    private AWSElasticsearchConfigurationProvider configurator;

    public void indexSegments(String triggerId, List<Segment> segments) {
        RepositoryFactory<SegmentType> esfactory = new RepositoryFactory<SegmentType>(configurator);

        Repository<SegmentType> repository = esfactory.initManager();
        repository.initClient();

        if (CollectionUtils.isNotEmpty(segments)) {
            for (Segment segment : segments) {
                indexSegment(triggerId, segment, repository);
            }
        }
        repository.closeClient();

    }

    private String indexSegment(String triggerId, Segment segment, Repository<SegmentType> repository) {
        SegmentMapping mapping = SegmentMapping.getInstance();

        // index document
        SegmentType documentIndexed = mapping.getDocumentType(segment);
        documentIndexed.setTriggerId(triggerId);
        String response = repository.indexMapping(mapping, documentIndexed);
        return response;
    }

    public Segment getSegment(String segmentId) {
        RepositoryFactory<SegmentType> esfactory = new RepositoryFactory<SegmentType>(configurator);
        Repository<SegmentType> repository = esfactory.initManager();
        repository.initClient();

        SegmentMapping mapping = SegmentMapping.getInstance();

        SearchResponse<Segment> response = repository.find(segmentId, mapping);

        Segment segment = response.getSource();

        repository.closeClient();
        return segment;
    }

    public List<Segment> getSegments(String triggerId) {
        RepositoryFactory<SegmentType> esfactory = new RepositoryFactory<SegmentType>(configurator);
        Repository<SegmentType> repository = esfactory.initManager();
        repository.initClient();

        List<Segment> segments = getSegments(triggerId, repository);

        repository.closeClient();
        return segments;
    }

    private List<Segment> getSegments(String triggerId, Repository<SegmentType> repository) {
        SegmentMapping mapping = SegmentMapping.getInstance();

        BoolQueryBuilder filters = QueryBuilders.boolQuery();
        filters = filters.must(QueryBuilders.matchQuery("triggerId", triggerId));
        SearchResponse<Segment> response = repository.searchWithFilters(filters, mapping);

        List<Segment> segments = response.getSources();

        return segments;
    }

    public void updateSegments(String triggerId, List<Segment> newSegments) {
        RepositoryFactory<SegmentType> esfactory = new RepositoryFactory<SegmentType>(configurator);
        Repository<SegmentType> repository = esfactory.initManager();
        repository.initClient();

        List<Segment> oldSegments = getSegments(triggerId, repository);
        Set<String> oldSegmentIds = oldSegments.stream().map(Segment::getId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(newSegments)) {
            for (Segment segment : newSegments) {
                if (segment.getId() != null) {
                    oldSegmentIds.remove(segment.getId());
                    updateSegment(triggerId, segment, repository);
                } else {
                    indexSegment(triggerId, segment, repository);
                }
            }
        }
        for (String segmentsToRemove : oldSegmentIds) {
            deleteSegment(segmentsToRemove, repository);
        }
        repository.closeClient();

    }

    private void deleteSegment(String segmentId, Repository<SegmentType> repository) {
        SegmentMapping mapping = SegmentMapping.getInstance();
        repository.removeMapping(segmentId, mapping);
    }

    private String updateSegment(String triggerId, Segment segment, Repository<SegmentType> repository) {
        SegmentMapping mapping = SegmentMapping.getInstance();

        // index document
        SegmentType documentIndexed = mapping.getDocumentType(segment);
        documentIndexed.setTriggerId(triggerId);
        String response = repository.updateMapping(segment.getId(), mapping, documentIndexed);
        return response;

    }

    public void setConfigurator(AWSElasticsearchConfigurationProvider configurator) {
        this.configurator = configurator;
    }

}
