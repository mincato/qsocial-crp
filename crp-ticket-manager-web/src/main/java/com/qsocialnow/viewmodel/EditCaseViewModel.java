package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.RegistryListView;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.ActionRegistryService;

@VariableResolver(DelegatingVariableResolver.class)
public class EditCaseViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private CaseService caseService;

    @WireVariable
    private ActionRegistryService actionRegistryService;

    private Case currentCase;

    private int pageSize = 15;

    private int activePage = 0;

    private String caseId;

    private boolean moreResults;

    private String keyword;

    private String action;

    private String user;

    private Date fromDate;

    private Date toDate;

    private List<RegistryListView> registries = new ArrayList<>();

    private List<ActionType> actionOptions = new ArrayList<>();

    private ActionType selectedAction;

    @Init
    public void init(@QueryParam("case") String caseSelected) {
        this.caseId = caseSelected;
        findCase(this.caseId);
        findRegistriesByCase(this.caseId);
        actionOptions = Arrays.asList(ActionType.values());
    }

    public List<RegistryListView> getRegistries() {
        return registries;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public boolean isMoreResults() {
        return moreResults;
    }

    public List<ActionType> getActionOptions() {
        return actionOptions;
    }

    public ActionType getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(ActionType selectedAction) {
        this.selectedAction = selectedAction;
    }

    public Case getCurrentCase() {
        return currentCase;
    }

    @Command
    @NotifyChange({ "registries", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findRegistriesByCase(this.caseId);
    }

    @Command
    @NotifyChange("currentCase")
    public void clear() {

    }

    private void findCase(String caseSelected) {

    }

    @GlobalCommand
    @NotifyChange({ "registries", "moreResults" })
    public void search() {
        this.findRegistriesBy();
    }

    @GlobalCommand
    @NotifyChange({ "currentCase", "selectedAction" })
    public void actionExecuted(@BindingParam("caseUpdated") Case caseUpdated) {
        this.currentCase = caseUpdated;
        this.selectedAction = null;
    }

    private PageResponse<RegistryListView> findRegistriesByCase(String caseSelected) {
        PageResponse<RegistryListView> pageResponse = actionRegistryService.findRegistries(activePage, pageSize,
                caseSelected);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.registries.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    private PageResponse<RegistryListView> findRegistriesBy() {
        PageResponse<RegistryListView> pageResponse = actionRegistryService.findRegistriesBy(activePage, pageSize,
                this.caseId, this.keyword, this.action, this.user, this.fromDate, this.toDate);
        this.registries.clear();
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.registries.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
