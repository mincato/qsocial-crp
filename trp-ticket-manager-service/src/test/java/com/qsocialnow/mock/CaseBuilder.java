package com.qsocialnow.mock;

import com.qsocialnow.common.model.cases.CaseListView;

public class CaseBuilder {

    public static CaseListView createCase1ListView() {
        CaseListView caseListView = new CaseListView();
        caseListView.setId("1");
        caseListView.setTitle("Case 1");
        return caseListView;
    }

    public static CaseListView createCase2ListView() {
        CaseListView caseListView = new CaseListView();
        caseListView.setId("2");
        caseListView.setTitle("Case 2");
        return caseListView;
    }

    public static CaseListView createCase3ListView() {
        CaseListView caseListView = new CaseListView();
        caseListView.setId("3");
        caseListView.setTitle("Case 3");
        return caseListView;
    }

}
