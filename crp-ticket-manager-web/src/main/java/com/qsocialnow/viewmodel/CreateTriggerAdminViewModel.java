package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.Domain;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateTriggerAdminViewModel implements Serializable {

    private static final long serialVersionUID = -8475951019777234240L;

    private boolean createTrigger = true;

    private boolean createSegment = false;

    public boolean isCreateTrigger() {
        return createTrigger;
    }

    public boolean isCreateSegment() {
        return createSegment;
    }

    @Init
    public void init() {
        createTrigger = true;
        createSegment = false;
    }

    @GlobalCommand
    @NotifyChange({ "createTrigger", "createSegment" })
    public void goToTrigger() {
        createTrigger = true;
        createSegment = false;
    }

    @GlobalCommand
    @NotifyChange({ "createTrigger", "createSegment" })
    public void goToSegment(@BindingParam("currentDomain") Domain currentDomain) {
        createTrigger = false;
        createSegment = true;
        Map<String, Object> args = new HashMap<>();
        args.put("currentDomain", currentDomain);
        System.out.println("amdin domain: " + currentDomain);
        BindUtils.postGlobalCommand(null, null, "initSegment", args);
    }

}
