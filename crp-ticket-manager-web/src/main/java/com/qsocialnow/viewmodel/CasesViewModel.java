package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.CaseService;

@VariableResolver(DelegatingVariableResolver.class)
public class CasesViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    private int pageSize = 15;
    private int activePage = 0;

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

    private PageResponse<CaseListView> findCases() {
        PageResponse<CaseListView> pageResponse = caseService.findAll(activePage, pageSize);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.cases.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }

        return pageResponse;
    }

}
