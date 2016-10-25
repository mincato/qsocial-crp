package com.qsocialnow.viewmodel.trigger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.NotifyCommand;
import org.zkoss.bind.annotation.ToClientCommand;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.NameByLanguage;
import com.qsocialnow.model.CategoryFilterView;
import com.qsocialnow.services.UserSessionService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class ChooseCategoriesViewModel implements Serializable {

    private static final long serialVersionUID = -2374328978702584103L;

    private CategoryFilterView filterCategory;

    @WireVariable
    private UserSessionService userSessionService;

    private boolean saved;

    @Command
    public void close(@ContextParam(ContextType.VIEW) Component comp) {
        comp.detach();
        if (saved) {
            Map<String, Object> args = new HashMap<>();
            args.put("filterCategory", filterCategory);
            BindUtils.postGlobalCommand(null, null, "addCategories", args);
        }
    }

    @Init
    public void init(@BindingParam("filterCategory") CategoryFilterView filterCategory) {
        this.filterCategory = filterCategory;
    }

    public CategoryFilterView getFilterCategory() {
        return filterCategory;
    }

    @Command
    public String createCategoryName(NameByLanguage category) {
        String language = userSessionService.getLanguage();
        return category.getNameByLanguage(language);
    }

    @Command
    @NotifyChange({ "saved" })
    public void save() {
        saved = true;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

}
