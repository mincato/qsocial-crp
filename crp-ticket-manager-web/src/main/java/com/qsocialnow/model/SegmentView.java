package com.qsocialnow.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.TeamListView;

public class SegmentView {

    @Valid
    private Segment segment;

    @NotNull(message = "app.field.empty.validation")
    private TeamListView team;

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public void setTeam(TeamListView team) {
        this.team = team;
    }

    public TeamListView getTeam() {
        return team;
    }
}
