package com.qsocialnow.viewmodel.subjectcategoryset;

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

import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.services.SubjectCategorySetService;

@VariableResolver(DelegatingVariableResolver.class)
public class CreateSubjectCategorySetViewModel implements Serializable {

    private static final long serialVersionUID = -2120461168021232109L;

    @WireVariable
    private SubjectCategorySetService subjectCategorySetService;

    private SubjectCategorySetView currentSubjectCategorySet;

    @Init
    public void init() {
        initSubjectCategorySet();
    }

    private void initSubjectCategorySet() {
        currentSubjectCategorySet = new SubjectCategorySetView();
        currentSubjectCategorySet.setCategories(new ArrayList<SubjectCategoryView>());
    }

    @Command
    @NotifyChange({ "currentSubjectCategorySet" })
    public void save() {
        SubjectCategorySet newSubjectCategorySet = new SubjectCategorySet();
        newSubjectCategorySet.setDescription(currentSubjectCategorySet.getDescription());
        newSubjectCategorySet.setCategories(currentSubjectCategorySet.getCategories().stream().map(category -> {
            SubjectCategory newCategory = new SubjectCategory();
            newCategory.setDescription(category.getDescription());
            return newCategory;
        }).collect(Collectors.toList()));
        newSubjectCategorySet = subjectCategorySetService.create(newSubjectCategorySet);
        Clients.showNotification(Labels.getLabel("subjectcategoryset.create.notification.success",
                new String[] { newSubjectCategorySet.getDescription() }));
        initSubjectCategorySet();
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

    @Command
    @NotifyChange({ "currentSubjectCategorySet" })
    public void clear() {
        initSubjectCategorySet();
    }

    public SubjectCategorySetView getCurrentSubjectCategorySet() {
        return currentSubjectCategorySet;
    }

    public void setCurrentSubjectCategorySet(SubjectCategorySetView currentSubjectCategorySet) {
        this.currentSubjectCategorySet = currentSubjectCategorySet;
    }

}