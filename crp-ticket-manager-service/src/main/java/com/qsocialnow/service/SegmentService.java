package com.qsocialnow.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.persistence.SegmentRepository;

@Service
public class SegmentService {

    @Autowired
    private SegmentRepository segmentRepository;

    public List<String> findAllActiveIdsByTeam(String teamId) {
        List<String> segmentsIds = segmentRepository.findAllActiveIdsByTeam(teamId);
        return segmentsIds;
    }

    public String reassignNewTeam(String oldTeamId, String newTeamId) {
        segmentRepository.reassignNewTeam(oldTeamId, newTeamId);
        return newTeamId;
    }

    public SegmentRepository getSegmentRepository() {
        return segmentRepository;
    }

    public void setSegmentRepository(SegmentRepository segmentRepository) {
        this.segmentRepository = segmentRepository;
    }
}
