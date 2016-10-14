package com.qsocialnow.persistence;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.config.TeamListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.elasticsearch.services.config.TeamService;

@Service
public class TeamRepository {

    private Logger log = LoggerFactory.getLogger(TeamRepository.class);

    @Autowired
    private TeamService teamElasticService;

    public Team save(Team newTeam) {
        try {
            String id = teamElasticService.indexTeam(newTeam);
            newTeam.setId(id);

            return newTeam;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

    public List<TeamListView> findAll(PageRequest pageRequest, String name) {
        List<TeamListView> teams = new ArrayList<>();

        try {
            Integer offset = pageRequest != null ? pageRequest.getOffset() : null;
            Integer limit = pageRequest != null ? pageRequest.getLimit() : null;
            List<Team> teamsRepo = teamElasticService.getTeams(offset, limit, name);

            for (Team teamRepo : teamsRepo) {
                TeamListView teamListView = new TeamListView();
                teamListView.setId(teamRepo.getId());
                teamListView.setName(teamRepo.getName());

                teams.add(teamListView);
            }
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return teams;
    }

    public Team findOne(String teamId) {
        Team team = null;

        try {
            team = teamElasticService.findOne(teamId);
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return team;
    }

    public Team update(Team team) {
        try {
            String id = teamElasticService.updateTeam(team);
            team.setId(id);
            return team;
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
        return null;
    }

}
