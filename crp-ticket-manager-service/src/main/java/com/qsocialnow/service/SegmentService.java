package com.qsocialnow.service;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qsocialnow.persistence.SegmentRepository;

@Service
public class SegmentService {

    private static final Logger log = LoggerFactory.getLogger(SegmentService.class);

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private CuratorFramework zookeeperClient;

    @Value("${app.domains.base.path}")
    private String domainsBasePath;

    public List<String> findAllActiveIdsByTeam(String teamId) {
        List<String> segmentsIds = segmentRepository.findAllActiveIdsByTeam(teamId);
        return segmentsIds;
    }

    public String reassignNewTeam(String oldTeamId, String newTeamId) {
        segmentRepository.reassignNewTeam(oldTeamId, newTeamId);
        try {
            zookeeperClient.setData().forPath(domainsBasePath);
        } catch (Exception e) {
            log.error("There was an error updating domains base path", e);
            throw new RuntimeException(e);
        }
        return newTeamId;
    }

    public SegmentRepository getSegmentRepository() {
        return segmentRepository;
    }

    public void setSegmentRepository(SegmentRepository segmentRepository) {
        this.segmentRepository = segmentRepository;
    }
}
