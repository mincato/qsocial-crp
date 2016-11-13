package com.qsocialnow.elasticsearch.mappings.config;

import org.json.simple.JSONObject;

import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.elasticsearch.mappings.Mapping;
import com.qsocialnow.elasticsearch.mappings.types.config.TeamType;

public class TeamMapping implements Mapping<TeamType, Team> {

    private static final String TYPE = "teams";

    private static TeamMapping instance;

    private final String index;

    private TeamMapping(String index) {
        this.index = index;
    }

    public static TeamMapping getInstance(String index) {
        if (instance == null)
            instance = new TeamMapping(index);
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
        return TeamType.class;
    }

    @Override
    public TeamType getDocumentType(Team document) {
        TeamType teamType = new TeamType();
        teamType.setName(document.getName());
        teamType.setUserResolvers(document.getUserResolvers());
        teamType.setUsers(document.getUsers());
        teamType.setActive(document.isActive());
        return teamType;
    }

    @Override
    public Team getDocument(TeamType documentType) {
        Team team = new Team();
        team.setId(documentType.getId());
        team.setName(documentType.getName());
        team.setUserResolvers(documentType.getUserResolvers());
        team.setUsers(documentType.getUsers());
        team.setActive(documentType.isActive());
        return team;
    }

}
