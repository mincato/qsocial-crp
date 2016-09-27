package com.qsocialnow.viewmodel;

import java.io.Serializable;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.model.TagCaseCategorySetView;
import com.qsocialnow.services.CaseCategorySetService;

@VariableResolver(DelegatingVariableResolver.class)
public class ChooseCaseCategoriesViewModel implements Serializable {

    private static final long serialVersionUID = -7485400424668315566L;

    private TagCaseCategorySetView caseCategorySet;

    @WireVariable
    private CaseCategorySetService caseCategorySetService;

    @Command
    public void close(@ContextParam(ContextType.VIEW) Component comp) {
        comp.detach();
    }

    @Init
    public void init(@BindingParam("caseCategorySet") TagCaseCategorySetView caseCategorySet) {
        this.caseCategorySet = caseCategorySet;
        if (caseCategorySet.getCategorySet().getCategories() == null) {
            caseCategorySet.getCategorySet().setCategories(
                    caseCategorySetService.findCategories(caseCategorySet.getCategorySet().getId()));
        }
        if (caseCategorySet.getCategories() != null) {
            caseCategorySet.getCategories().clear();
        }
    }

    public TagCaseCategorySetView getCaseCategorySet() {
        return caseCategorySet;
    }
}
