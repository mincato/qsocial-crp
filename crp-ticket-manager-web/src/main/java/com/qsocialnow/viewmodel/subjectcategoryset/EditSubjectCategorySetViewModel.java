package com.qsocialnow.viewmodel.subjectcategoryset;

import java.io.Serializable;
import java.util.HashMap;
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

import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.services.SubjectCategorySetService;

@VariableResolver(DelegatingVariableResolver.class)
@NotifyCommand(value = "modal$closeEvent", onChange = "_vm_.saved")
@ToClientCommand("modal$closeEvent")
public class EditSubjectCategorySetViewModel implements Serializable {

    private static final long serialVersionUID = -39541127130255335L;

    @WireVariable
    private SubjectCategorySetService subjectCategorySetService;

    private String subjectCategorySetId;

    private SubjectCategorySetView currentSubjectCategorySet;

    private boolean saved;

    @Init
    public void init(@BindingParam("subjectcategoryset") String subjectCategorySet) {
        subjectCategorySetId = subjectCategorySet;
        initSubjectCategorySet(subjectCategorySetId);
    }

    private void initSubjectCategorySet(String subjectCategorySetId) {
        SubjectCategorySet subjectCategorySet = subjectCategorySetService.findOne(subjectCategorySetId);
        currentSubjectCategorySet = new SubjectCategorySetView(subjectCategorySet);
    }

    @Command
    @NotifyChange({ "currentSubjectCategorySet", "saved" })
    public void save() {
        SubjectCategorySet subjectCategorySet = new SubjectCategorySet();
        subjectCategorySet.setId(currentSubjectCategorySet.getId());
        subjectCategorySet.setDescription(currentSubjectCategorySet.getDescription());
        subjectCategorySet.setCategories(currentSubjectCategorySet.getCategories().stream().map(categoryView -> {
            SubjectCategory category = new SubjectCategory();
            category.setId(categoryView.getId());
            category.setDescription(categoryView.getDescription());
            category.setActive(categoryView.isActive());
            return category;
        }).collect(Collectors.toList()));
        subjectCategorySet.setActive(currentSubjectCategorySet.isActive());
        subjectCategorySet = subjectCategorySetService.update(subjectCategorySet);
        Clients.showNotification(Labels.getLabel("subjectcategoryset.edit.notification.success",
                new String[] { subjectCategorySet.getDescription() }));

        saved = true;
    }

    @Command
    public void addCategory(@BindingParam("fx") SubjectCategorySetView subjectCategorySet) {
        subjectCategorySet.getCategories().add(new SubjectCategoryView());
        BindUtils.postNotifyChange(null, null, subjectCategorySet, "categories");
    }

    @Command
    public void deleteCategory(@BindingParam("index") int idx,
            @BindingParam("fx") SubjectCategorySetView subjectCategorySet) {
        subjectCategorySet.getCategories().remove(idx);
        BindUtils.postNotifyChange(null, null, subjectCategorySet, "categories");
    }

    public boolean isSaved() {
        return saved;
    }

    public SubjectCategorySetView getCurrentSubjectCategorySet() {
        return currentSubjectCategorySet;
    }

    public void setCurrentSubjectCategorySet(SubjectCategorySetView currentSubjectCategorySet) {
        this.currentSubjectCategorySet = currentSubjectCategorySet;
    }

    @Command
    public void close(@ContextParam(ContextType.VIEW) Div comp) {
        comp.detach();
        if (saved) {
            Map<String, Object> args = new HashMap<String, Object>();
            args.put("subjectCategorySetChanged", currentSubjectCategorySet);
            BindUtils.postGlobalCommand(null, null, "changeSubjectCategorySet", args);
        }
    }

}
