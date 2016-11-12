package com.qsocialnow.viewmodel.trigger;

import java.io.Serializable;

import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.converters.DateInputConverter;
import com.qsocialnow.services.UserSessionService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateTriggerAdminViewModel implements Serializable {

    private static final long serialVersionUID = -8475951019777234240L;

    private DateInputConverter dateConverter = new DateInputConverter();

    private boolean createTrigger = true;

    private boolean createSegment = false;

    private boolean createCriteria = false;

    private boolean createAction = false;

    @WireVariable
    private UserSessionService userSessionService;

    public DateInputConverter getDateConverter() {
        return dateConverter;
    }

    public boolean isCreateTrigger() {
        return createTrigger;
    }

    public boolean isCreateSegment() {
        return createSegment;
    }

    public boolean isCreateCriteria() {
        return createCriteria;
    }

    public boolean isCreateAction() {
        return createAction;
    }

    @Init
    public void init() {
        createTrigger = true;
        createSegment = false;
        createCriteria = false;
        Clients.scrollTo(0, 0);
    }

    @GlobalCommand
    @NotifyChange({ "createTrigger", "createSegment", "createCriteria", "createAction" })
    public void goToTrigger() {
        createTrigger = true;
        createSegment = false;
        createCriteria = false;
        createAction = false;
        Clients.scrollTo(0, 0);
    }

    @GlobalCommand
    @NotifyChange({ "createTrigger", "createSegment", "createCriteria", "createAction" })
    public void goToSegment() {
        createTrigger = false;
        createSegment = true;
        createCriteria = false;
        createAction = false;
        Clients.scrollTo(0, 0);
    }

    @GlobalCommand
    @NotifyChange({ "createTrigger", "createSegment", "createCriteria", "createAction" })
    public void goToCriteria() {
        createTrigger = false;
        createSegment = false;
        createCriteria = true;
        createAction = false;
        Clients.scrollTo(0, 0);
    }

    @GlobalCommand
    @NotifyChange({ "createTrigger", "createSegment", "createCriteria", "createAction" })
    public void goToAction() {
        createTrigger = false;
        createSegment = false;
        createCriteria = false;
        createAction = true;
        Clients.scrollTo(0, 0);
    }

}
