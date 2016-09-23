package com.qsocialnow.viewmodel.trigger;

import java.io.Serializable;

import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateTriggerAdminViewModel implements Serializable {

    private static final long serialVersionUID = -8475951019777234240L;

    private boolean createTrigger = true;

    private boolean createSegment = false;

    private boolean createCriteria = false;

    public boolean isCreateTrigger() {
        return createTrigger;
    }

    public boolean isCreateSegment() {
        return createSegment;
    }

    public boolean isCreateCriteria() {
        return createCriteria;
    }

    @Init
    public void init() {
        createTrigger = true;
        createSegment = false;
        createCriteria = false;
    }

    @GlobalCommand
    @NotifyChange({ "createTrigger", "createSegment", "createCriteria" })
    public void goToTrigger() {
        createTrigger = true;
        createSegment = false;
        createCriteria = false;
    }

    @GlobalCommand
    @NotifyChange({ "createTrigger", "createSegment", "createCriteria" })
    public void goToSegment() {
        createTrigger = false;
        createSegment = true;
        createCriteria = false;
    }

    @GlobalCommand
    @NotifyChange({ "createTrigger", "createSegment", "createCriteria" })
    public void goToCriteria() {
        createTrigger = false;
        createSegment = false;
        createCriteria = true;
    }

}
