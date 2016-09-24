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
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.CaseCategorySet;
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
            return newCategory;
        }).collect(Collectors.toList()));
        newCaseCategorySet = caseCategorySetService.create(newCaseCategorySet);
        Clients.showNotification(Labels.getLabel("casecategoryset.create.notification.success",
                new String[] { newCaseCategorySet.getDescription() }));
        initCaseCategorySet();
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