package com.qsocialnow.viewmodel.casecategoryset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Div;

import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.services.CaseCategorySetService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class EditCaseCategorySetViewModel implements Serializable {

    private static final long serialVersionUID = -39541127130255335L;

    @WireVariable
    private CaseCategorySetService caseCategorySetService;

    private String caseCategorySetId;

    private CaseCategorySetView currentCaseCategorySet;

    private boolean saved;

    @Init
    public void init(@BindingParam("casecategoryset") String caseCategorySet) {
        caseCategorySetId = caseCategorySet;
        initCaseCategorySet(caseCategorySetId);
    }

    private void initCaseCategorySet(String caseCategorySetId) {
        CaseCategorySet caseCategorySet = caseCategorySetService.findOne(caseCategorySetId);
        currentCaseCategorySet = new CaseCategorySetView(caseCategorySet);
    }

    @Command
    @NotifyChange({ "currentCaseCategorySet", "saved" })
    public void save() {
        CaseCategorySet caseCategorySet = new CaseCategorySet();
        caseCategorySet.setId(currentCaseCategorySet.getId());
        caseCategorySet.setDescription(currentCaseCategorySet.getDescription());
        caseCategorySet.setCategories(currentCaseCategorySet.getCategories().stream().map(categoryView -> {
            CaseCategory category = new CaseCategory();
            category.setId(categoryView.getId());
            category.setDescription(categoryView.getDescription());
            if (currentCaseCategorySet.getActive()) {
                category.setActive(currentCaseCategorySet.getActive());
            } else {
                category.setActive(false);
            }
            return category;
        }).collect(Collectors.toList()));
        caseCategorySet.setActive(currentCaseCategorySet.getActive());
        caseCategorySet = caseCategorySetService.update(caseCategorySet);
        Clients.showNotification(Labels.getLabel("casecategoryset.edit.notification.success",
                new String[] { caseCategorySet.getDescription() }));

        saved = true;
    }

    @Command
    public void addCategory(@BindingParam("fx") CaseCategorySetView caseCategorySet) {
        if (caseCategorySet.getCategories() == null) {
            List<CaseCategoryView> categories = new ArrayList<>();
            caseCategorySet.setCategories(categories);
        }
        caseCategorySet.getCategories().add(new CaseCategoryView());
        BindUtils.postNotifyChange(null, null, caseCategorySet, "categories");
    }

    @Command
    public void deleteCategory(@BindingParam("index") int idx, @BindingParam("fx") CaseCategorySetView caseCategorySet) {
        caseCategorySet.getCategories().remove(idx);
        BindUtils.postNotifyChange(null, null, caseCategorySet, "categories");
    }

    public boolean isSaved() {
        return saved;
    }

    public CaseCategorySetView getCurrentCaseCategorySet() {
        return currentCaseCategorySet;
    }

    public void setCurrentCaseCategorySet(CaseCategorySetView currentCaseCategorySet) {
        this.currentCaseCategorySet = currentCaseCategorySet;
    }

    @Command
    public void close(@ContextParam(ContextType.VIEW) Div comp) {
        comp.detach();
        if (saved) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("caseCategorySetChanged", currentCaseCategorySet);
            BindUtils.postGlobalCommand(null, null, "changeCaseCategorySet", args);
        }
    }

}
