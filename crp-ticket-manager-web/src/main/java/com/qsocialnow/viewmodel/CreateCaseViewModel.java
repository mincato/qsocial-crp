package com.qsocialnow.viewmodel;

import java.io.Serializable;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.model.CaseView;
import com.qsocialnow.services.CaseService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateCaseViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private CaseService caseService;

    private CaseView currentCase;

    public CaseView getCurrentCase() {
        return currentCase;
    }

    @Init
    public void init() {
        initCase();
    }

    private void initCase() {
        currentCase = new CaseView();
        currentCase.setNewCase(Case.getNewCase());
    }

    @Command
    @NotifyChange("currentCase")
    public void save() {

        Case newCase = currentCase.getNewCase();
        currentCase.setNewCase(caseService.create(newCase));
        Clients.showNotification(Labels.getLabel("cases.create.notification.success", new String[] { currentCase
                .getNewCase().getId() }));
        initCase();
    }

    @Command
    @NotifyChange({ "currentCase" })
    public void clear() {
        initCase();
    }
}