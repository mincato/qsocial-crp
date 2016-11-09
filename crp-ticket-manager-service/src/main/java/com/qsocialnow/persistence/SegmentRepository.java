package com.qsocialnow.persistence;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.elasticsearch.services.config.SegmentService;

@Service
public class SegmentRepository implements ReportRepository {

    @Autowired
    private SegmentService segmentElasticService;

    public List<Segment> findSegmentsByTeams(List<Team> teams) {
        List<Segment> segments = segmentElasticService.getSegmentsByTeams(teams);
        return segments;
    }

    public Map<String, String> findAllReport() {
        return segmentElasticService.getAllSegmentsAsMap();
    }

    public List<String> findAllActiveIdsByTeam(String teamId) {
        List<String> segmentIds = segmentElasticService.getActiveIdsByTeam(teamId);
        return segmentIds;
    }

    public void reassignNewTeam(String oldTeamId, String newTeamId) {
        segmentElasticService.reassignNewTeam(oldTeamId, newTeamId);
    }
}
