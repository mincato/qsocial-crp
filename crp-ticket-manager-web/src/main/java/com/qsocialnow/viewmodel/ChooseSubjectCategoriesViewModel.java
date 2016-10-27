package com.qsocialnow.viewmodel;

import java.io.Serializable;

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
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.model.TagSubjectCategorySetView;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class ChooseSubjectCategoriesViewModel implements Serializable {

    private static final long serialVersionUID = 1670037422141566180L;

    private TagSubjectCategorySetView subjectCategorySet;

    private boolean saved;

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
