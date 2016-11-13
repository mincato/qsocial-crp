package com.qsocialnow.viewmodel.casecategoryset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.handler.NotificationHandler;
import com.qsocialnow.services.CaseCategorySetService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateCaseCategorySetViewModel implements Serializable {

    private static final long serialVersionUID = -2753454041524482163L;

    @WireVariable
    private CaseCategorySetService caseCategorySetService;

    private CaseCategorySetView currentCaseCategorySet;

    @Init
    public void init() {
        initCaseCategorySet();
    }

    private void initCaseCategorySet() {
        currentCaseCategorySet = new CaseCategorySetView();
        currentCaseCategorySet.setActive(true);
        currentCaseCategorySet.setCategories(new ArrayList<CaseCategoryView>());
    }

    @Command
    @NotifyChange({ "currentCaseCategorySet" })
    public void save() {
        CaseCategorySet newCaseCategorySet = new CaseCategorySet();
        newCaseCategorySet.setDescription(currentCaseCategorySet.getDescription());
        newCaseCategorySet.setCategories(currentCaseCategorySet.getCategories().stream().map(category -> {
            CaseCategory newCategory = new CaseCategory();
            newCategory.setDescription(category.getDescription());
            newCategory.setActive(currentCaseCategorySet.getActive());
            return newCategory;
        }).collect(Collectors.toList()));
        newCaseCategorySet.setActive(currentCaseCategorySet.getActive());
        newCaseCategorySet = caseCategorySetService.create(newCaseCategorySet);
        NotificationHandler.addNotification(Labels.getLabel("casecategoryset.create.notification.success",
                new String[] { newCaseCategorySet.getDescription() }));
        Executions.getCurrent().sendRedirect("/pages/case-category-set/list/index.zul");
    }

    @Command
    public void addCategory(@BindingParam("fx") CaseCategorySetView caseCategorySet) {
        caseCategorySet.getCategories().add(new CaseCategoryView());
        BindUtils.postNotifyChange(null, null, caseCategorySet, "categories");
    }

    @Command
    public void deleteCategory(@BindingParam("index") int idx, @BindingParam("fx") CaseCategorySetView caseCategorySet) {
        caseCategorySet.getCategories().remove(idx);
        BindUtils.postNotifyChange(null, null, caseCategorySet, "categories");
    }

    @Command
    @NotifyChange({ "currentCaseCategorySet" })
    public void clear() {
        initCaseCategorySet();
    }

    public CaseCategorySetView getCurrentCaseCategorySet() {
        return currentCaseCategorySet;
    }

    public void setCurrentCaseCategorySet(CaseCategorySetView currentCaseCategorySet) {
        this.currentCaseCategorySet = currentCaseCategorySet;
    }

}