package com.qsocialnow.viewmodel.trigger;

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

    private SegmentView fxSegment;

    private Domain currentDomain;

    private List<TeamListView> teamOptions;

    private boolean editing;

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
        editing = false;
    }

    @GlobalCommand
    @NotifyChange({ "currentSegment" })
    public void editSegment(@BindingParam("currentDomain") Domain currentDomain,
            @BindingParam("segment") Segment segment) {
        this.currentSegment.setSegment(segment);
        this.currentSegment.setTeam(teamOptions.stream().filter(team -> team.getId().equals(segment.getTeam()))
                .findFirst().get());
        this.currentDomain = currentDomain;
        editing = true;
    }

    @Command
    @NotifyChange({ "currentSegment" })
    public void save() {
        Map<String, Object> args = new HashMap<String, Object>();
        Segment segment = currentSegment.getSegment();
        segment.setTeam(currentSegment.getTeam().getId());
        if (editing) {
            BindUtils.postGlobalCommand(null, null, "updateSegment", new HashMap<>());
        } else {
            args.put("segment", currentSegment.getSegment());
            BindUtils.postGlobalCommand(null, null, "addSegment", args);
        }
    }

    @Command
    public void createNewCriteria(@BindingParam("fxSegment") SegmentView fxSegment) {
        this.fxSegment = fxSegment;
        Map<String, Object> args = new HashMap<>();
        args.put("currentDomain", currentDomain);
        BindUtils.postGlobalCommand(null, null, "goToCriteria", new HashMap<>());
        BindUtils.postGlobalCommand(null, null, "initCriteria", args);
    }

    @Command
    public void editCriteria(@BindingParam("fxSegment") SegmentView fxSegment,
            @BindingParam("criteria") DetectionCriteria detectionCriteria) {
        this.fxSegment = fxSegment;
        Map<String, Object> args = new HashMap<>();
        args.put("currentDomain", currentDomain);
        args.put("detectionCriteria", detectionCriteria);
        BindUtils.postGlobalCommand(null, null, "goToCriteria", new HashMap<>());
        BindUtils.postGlobalCommand(null, null, "editCriteria", args);
    }

    @Command
    @NotifyChange({ "currentSegment" })
    public void cancel() {
        currentSegment.setSegment(new Segment());
        currentSegment.setTeam(null);
        BindUtils.postGlobalCommand(null, null, "goToTrigger", new HashMap<>());
    }

    @GlobalCommand
    public void addCriteria(@BindingParam("detectionCriteria") DetectionCriteria detectionCriteria) {
        this.fxSegment.getSegment().getDetectionCriterias().add(detectionCriteria);
        BindUtils.postGlobalCommand(null, null, "goToSegment", new HashMap<>());
        BindUtils.postNotifyChange(null, null, this.fxSegment, "segment");
        this.fxSegment = null;
    }

    @GlobalCommand
    public void updateCriteria() {
        BindUtils.postGlobalCommand(null, null, "goToSegment", new HashMap<>());
        BindUtils.postNotifyChange(null, null, this.fxSegment, "segment");
        this.fxSegment = null;
    }

    @Command
    @NotifyChange("currentSegment")
    public void removeCriteria(@BindingParam("fxSegment") SegmentView fxSegment,
            @BindingParam("criteria") DetectionCriteria detectionCriteria) {
        fxSegment.getSegment().getDetectionCriterias().remove(detectionCriteria);
        BindUtils.postNotifyChange(null, null, fxSegment, "segment");
    }

}
