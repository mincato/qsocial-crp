package com.qsocialnow.elasticsearch.mappings.types.config;

import com.qsocialnow.common.model.config.Team;

import io.searchbox.annotations.JestId;

public class TeamType extends Team {

    @JestId
    private String idTeam;

    @Override
    public String getId() {
        return this.idTeam;
    }

}
