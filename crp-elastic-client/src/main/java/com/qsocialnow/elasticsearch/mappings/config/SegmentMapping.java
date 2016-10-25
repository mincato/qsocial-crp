package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.SegmentType;

public class SegmentMapping implements Mapping<SegmentType, Segment> {

    private static final String TYPE = "segment";

    private static SegmentMapping instance;

    private final String index;

    private SegmentMapping(String index) {
        this.index = index;
    }

    public static SegmentMapping getInstance(String index) {
        if (instance == null)
            instance = new SegmentMapping(index);
        return instance;
    }

    @Override
    public String getIndex() {
        return index;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getMappingDefinition() {
        JSONObject mapping = new JSONObject();
        return mapping.toJSONString();
    }

    @Override
    public Class<?> getClassType() {
        return SegmentType.class;
    }

    @Override
    public SegmentType getDocumentType(Segment document) {
        SegmentType segmentType = new SegmentType();
        segmentType.setDescription(document.getDescription());
        segmentType.setDetectionCriterias(document.getDetectionCriterias());
        segmentType.setTeam(document.getTeam());
        return segmentType;
    }

    @Override
    public Segment getDocument(SegmentType documentType) {
        System.out.println(documentType.toString());
        Segment segment = new Segment();
        segment.setId(documentType.getId());
        segment.setDescription(documentType.getDescription());
        segment.setDetectionCriterias(documentType.getDetectionCriterias());
        segment.setTeam(documentType.getTeam());
        segment.setTriggerId(documentType.getTriggerId());
        return segment;
    }

}
