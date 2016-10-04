package com.qsocialnow.viewmodel;

import java.io.Serializable;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.model.TagSubjectCategorySetView;

@VariableResolver(DelegatingVariableResolver.class)
public class ChooseSubjectCategoriesViewModel implements Serializable {

    private static final long serialVersionUID = 1670037422141566180L;

    private TagSubjectCategorySetView subjectCategorySet;

    @Command
    public void close(@ContextParam(ContextType.VIEW) Component comp) {
        comp.detach();
    }

    @Init
    public void init(@BindingParam("subjectCategorySet") TagSubjectCategorySetView subjectCategorySet) {
        this.subjectCategorySet = subjectCategorySet;
    }

    public TagSubjectCategorySetView getSubjectCategorySet() {
        return subjectCategorySet;
    }
}
