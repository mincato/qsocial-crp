package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Segment;
import com.qsocialnow.common.model.config.TeamListView;
import com.qsocialnow.model.SegmentView;
import com.qsocialnow.services.TeamService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateSegmentViewModel implements Serializable {

    private static final long serialVersionUID = 7272332325414389826L;

    @WireVariable
    private TeamService teamService;

    private SegmentView currentSegment;

    private Domain currentDomain;

    private List<TeamListView> teamOptions;

    public SegmentView getCurrentSegment() {
        return currentSegment;
    }

    public List<TeamListView> getTeamOptions() {
        return teamOptions;
    }

    @Init
    public void init() {
        this.currentSegment = new SegmentView();
        this.currentSegment.setSegment(new Segment());
        this.teamOptions = teamService.findAll();
    }

    @GlobalCommand
    @NotifyChange({ "currentSegment" })
    public void initSegment(@BindingParam("currentDomain") Domain currentDomain) {
        this.currentSegment.setSegment(new Segment());
        this.currentSegment.setTeam(null);
        this.currentDomain = currentDomain;
        System.out.println("segment domain: " + currentDomain);
    }

    @Command
    @NotifyChange({ "currentSegment" })
    public void save() {
        Map<String, Object> args = new HashMap<String, Object>();
        Segment segment = currentSegment.getSegment();
        segment.setTeam(currentSegment.getTeam().getId());
        args.put("segment", currentSegment.getSegment());
        currentSegment.setSegment(new Segment());
        currentSegment.setTeam(null);
        BindUtils.postGlobalCommand(null, null, "addSegment", args);
    }

    @Command
    public void createNewCriteria() {
        Map<String, Object> args = new HashMap<>();
        args.put("currentDomain", currentDomain);
        System.out.println("segment domain: " + currentDomain);
        Executions.createComponents("create-criteria.zul", null, args);
    }

    @Command
    @NotifyChange({ "currentSegment" })
    public void cancel() {
        currentSegment.setSegment(new Segment());
        currentSegment.setTeam(null);
        BindUtils.postGlobalCommand(null, null, "goToTrigger", new HashMap<>());
    }

    @GlobalCommand
    @NotifyChange("currentSegment")
    public void addCriteria(@BindingParam("detectionCriteria") DetectionCriteria detectionCriteria) {
        this.currentSegment.getSegment().getDetectionCriterias().add(detectionCriteria);
    }

    @Command
    @NotifyChange("currentSegment")
    public void removeCriteria(@BindingParam("criteria") DetectionCriteria detectionCriteria) {
        this.currentSegment.getSegment().getDetectionCriterias().remove(detectionCriteria);
    }

}
