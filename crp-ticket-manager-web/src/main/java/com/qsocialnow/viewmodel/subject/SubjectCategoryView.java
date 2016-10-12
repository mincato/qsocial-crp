package com.qsocialnow.viewmodel.subject;

import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;

public class SubjectCategoryView {

    private SubjectCategorySet set;

    private SubjectCategory category;

    public SubjectCategorySet getSet() {
        return set;
    }

    public void setSet(SubjectCategorySet set) {
        this.set = set;
    }

    public SubjectCategory getCategory() {
        return category;
    }

    public void setCategory(SubjectCategory category) {
        this.category = category;
    }

}
