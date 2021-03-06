package com.qsocialnow.model;

import javax.validation.constraints.NotNull;

import com.qsocialnow.common.model.cases.SubjectListView;

public class ChangeSubjectActionView {

    @NotNull(message = "app.subject.null.validation")
    private SubjectListView selectedSubject;

    public SubjectListView getSelectedSubject() {
        return selectedSubject;
    }

    public void setSelectedSubject(SubjectListView selectedSubject) {
        this.selectedSubject = selectedSubject;
    }

}
