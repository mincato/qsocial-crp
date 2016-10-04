package com.qsocialnow.viewmodel.subject;

import java.util.List;

import javax.validation.Valid;

import com.qsocialnow.common.model.cases.Subject;
import com.qsocialnow.common.model.config.Media;

public class SubjectView {

    @Valid
    private Subject subject;

    private Media source;

    private List<SubjectCategoryView> categories;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Media getSource() {
        return source;
    }

    public void setSource(Media source) {
        this.source = source;
    }

    public List<SubjectCategoryView> getCategories() {
        return categories;
    }

    public void setCategories(List<SubjectCategoryView> categories) {
        this.categories = categories;
    }

}
