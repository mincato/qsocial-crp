package com.qsocialnow.persistence;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.elasticsearch.services.config.SegmentService;

@Service
public class SegmentRepository {

    private Logger log = LoggerFactory.getLogger(SegmentRepository.class);

    @Autowired
    private SegmentService segmentElasticService;

    public List<Segment> findSegmentsByTeams(List<Team> teams) {
        List<Segment> segments = segmentElasticService.getSegmentsByTeams(teams);
        return segments;
    }

}
