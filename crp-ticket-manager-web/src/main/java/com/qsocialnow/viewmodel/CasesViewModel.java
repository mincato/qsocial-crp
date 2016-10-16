package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.CaseService;

@VariableResolver(DelegatingVariableResolver.class)
public class CasesViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    private int pageSize = 15;

    private int activePage = 0;

    private String sortField = "openDate";

    private boolean sortOrder = false;

    @WireVariable
    private CaseService caseService;

    private boolean moreResults;

    private List<CaseListView> cases = new ArrayList<>();

    @Init
    public void init() {
        findCases();
    }

    public List<CaseListView> getCases() {
        return this.cases;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public boolean isMoreResults() {
        return moreResults;
    }

    @Command
    @NotifyChange({ "cases", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findCases();
    }

    @Command
    @NotifyChange({ "cases", "moreResults" })
    public void sortList(@BindingParam("sortField") String sortField) {
        this.sortField = sortField;
        this.sortOrder = this.sortOrder?!this.sortOrder:this.sortOrder;
        this.activePage = 0;
        this.cases.clear();
        this.findCases();
    }

    private PageResponse<CaseListView> findCases() {
    	PageRequest pageRequest = new PageRequest(activePage, pageSize, sortField);
    	pageRequest.setSortOrder(this.sortOrder);

        PageResponse<CaseListView> pageResponse = caseService.findAll(pageRequest);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.cases.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }

        return pageResponse;
    }

}
