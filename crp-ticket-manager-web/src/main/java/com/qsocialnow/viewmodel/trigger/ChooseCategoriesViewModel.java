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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.model.CategoryFilterView;

@VariableResolver(DelegatingVariableResolver.class)
public class ChooseCategoriesViewModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private CategoryFilterView filterCategory;

    @Command
    public void close(@ContextParam(ContextType.VIEW) Component comp) {
        comp.detach();
        Map<String, Object> args = new HashMap<>();
        args.put("filterCategory", filterCategory);
        BindUtils.postGlobalCommand(null, null, "addCategories", args);

    }

    @Init
    public void init(@BindingParam("filterCategory") CategoryFilterView filterCategory) {
        this.filterCategory = filterCategory;
    }

    public CategoryFilterView getFilterCategory() {
        return filterCategory;
    }
}
