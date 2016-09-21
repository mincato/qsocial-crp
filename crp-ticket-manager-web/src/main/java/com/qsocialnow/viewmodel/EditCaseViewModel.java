package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
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
import com.qsocialnow.services.ActionRegistryService;
import com.qsocialnow.services.CaseService;

@VariableResolver(DelegatingVariableResolver.class)
public class EditCaseViewModel implements Serializable {

    private static final String ALL_VALUES = "ALL_VALUES";

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private CaseService caseService;

    @WireVariable
    private ActionRegistryService actionRegistryService;

    private Case currentCase;

    private static final int PAGE_SIZE_DEFAULT = 15;

    private static final int ACTIVE_PAGE_DEFAULT = 0;

    private int pageSize = PAGE_SIZE_DEFAULT;
    private int activePage = ACTIVE_PAGE_DEFAULT;

    private boolean filterActive;

    private String caseId;

    private boolean moreResults;

    private String keyword;

    private String action;

    private String user;

    private Date fromDate;

    private Date toDate;

    private List<RegistryListView> registries = new ArrayList<>();

    private List<ActionType> actionOptions = new ArrayList<>();

    private List<String> actionFilterOptions = new ArrayList<>();

    private ActionType selectedAction;

    @Init
    public void init(@QueryParam("case") String caseSelected) {
        this.caseId = caseSelected;

        this.actionFilterOptions.add(0, ALL_VALUES);
        for (int i = 0; i < ActionType.values().length; i++) {
            this.actionFilterOptions.add(ActionType.values()[i].toString());
        }
        this.action = ALL_VALUES;
        findCase(this.caseId);
        findRegistriesBy();
        this.actionOptions = getAllowedActionsByCase();
    }

    @Command
    @NotifyChange({ "registries", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findRegistriesBy();
    }

    @Command
    @NotifyChange("currentCase")
    public void clear() {

    }

    private void findCase(String caseSelected) {
        this.currentCase = caseService.findById(caseSelected);
    }

    @GlobalCommand
    @NotifyChange({ "actionOptions" })
    private List<ActionType> getAllowedActionsByCase() {
        if (currentCase != null)
            return currentCase.getAllowedManualActions();
        else
            return new ArrayList<ActionType>();
    }

    @Command
    @NotifyChange({ "registries", "moreResults", "filterActive" })
    public void search() {
        this.filterActive = true;
        this.setDefaultPage();
        this.registries.clear();
        this.findRegistriesBy();
    }

    @GlobalCommand
    @NotifyChange({ "registries", "moreResults" })
    public void refreshRegistries() {
        this.registries.clear();
        this.filterActive = false;
        this.action = null;
        this.fromDate = null;
        this.toDate = null;
        this.user = null;
        this.keyword = null;
        this.setDefaultPage();
        this.findRegistriesBy();
    }

    @GlobalCommand
    @NotifyChange({ "currentCase", "selectedAction" })
    public void actionExecuted(@BindingParam("caseUpdated") Case caseUpdated) {
        this.currentCase = caseUpdated;
        this.selectedAction = null;
    }

    @Command
    public void onSelectAction() {
        Map<String, Object> args = new HashMap<>();
        args.put("currentCase", currentCase);
        args.put("action", selectedAction);
        BindUtils.postGlobalCommand(null, null, "show", args);
    }

    private PageResponse<RegistryListView> findRegistriesBy() {
        String actionValue = ALL_VALUES.equals(this.action) ? null : this.action;
        PageResponse<RegistryListView> pageResponse = actionRegistryService.findRegistriesBy(activePage, pageSize,
                this.caseId, this.keyword, actionValue, this.user, this.fromDate, this.toDate);

        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.registries.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    private void setDefaultPage() {
        this.pageSize = PAGE_SIZE_DEFAULT;
        this.activePage = ACTIVE_PAGE_DEFAULT;
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

    public List<String> getActionFilterOptions() {
        return actionFilterOptions;
    }

    public boolean isFilterActive() {
        return filterActive;
    }
}
