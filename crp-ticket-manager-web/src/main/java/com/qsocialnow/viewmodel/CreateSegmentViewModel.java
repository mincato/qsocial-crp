package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.DetectionCriteria;
import com.qsocialnow.common.model.config.Domain;
import com.qsocialnow.common.model.config.Segment;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateSegmentViewModel implements Serializable {

    private static final long serialVersionUID = 7272332325414389826L;

    private Segment currentSegment;

    private Domain currentDomain;

    public Segment getCurrentSegment() {
        return currentSegment;
    }

    @Init
    public void init() {
        this.currentSegment = new Segment();
    }

    @GlobalCommand
    @NotifyChange({ "currentSegment" })
    public void initSegment(@BindingParam("currentDomain") Domain currentDomain) {
        this.currentSegment = new Segment();
        this.currentDomain = currentDomain;
        System.out.println("segment domain: " + currentDomain);
    }

    @Command
    @NotifyChange({ "currentSegment" })
    public void save() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("segment", currentSegment);
        currentSegment = new Segment();
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
        currentSegment = new Segment();
        BindUtils.postGlobalCommand(null, null, "goToTrigger", new HashMap<>());
    }

    @GlobalCommand
    @NotifyChange("currentSegment")
    public void addCriteria(@BindingParam("detectionCriteria") DetectionCriteria detectionCriteria) {
        this.currentSegment.getDetectionCriterias().add(detectionCriteria);
    }

    @Command
    @NotifyChange("currentSegment")
    public void removeCriteria(@BindingParam("criteria") DetectionCriteria detectionCriteria) {
        this.currentSegment.getDetectionCriterias().remove(detectionCriteria);
    }

}
