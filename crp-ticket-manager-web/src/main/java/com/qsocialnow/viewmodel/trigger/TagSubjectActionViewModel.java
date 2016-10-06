package com.qsocialnow.viewmodel.trigger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.AutomaticActionCriteria;
import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.model.ListView;
import com.qsocialnow.model.TagSubjectActionView;
import com.qsocialnow.model.TagSubjectCategorySetView;
import com.qsocialnow.model.TriggerSubjectCategorySetView;
import com.qsocialnow.model.TriggerView;

@VariableResolver(DelegatingVariableResolver.class)
public class TagSubjectActionViewModel implements Serializable {

	private static final long serialVersionUID = 3970631278576411053L;

	private TagSubjectActionView tagSubjectAction;

    private ListView<SubjectCategorySet> categorySetListView;

    @Init
    public void init() {
        this.categorySetListView = new ListView<>();
        tagSubjectAction = new TagSubjectActionView();
    }

    public TagSubjectActionView getTagSubjectAction() {
        return tagSubjectAction;
    }

    public void setTagSubjectAction(TagSubjectActionView tagSubjectAction) {
		this.tagSubjectAction = tagSubjectAction;
	}

    public ListView<SubjectCategorySet> getCategorySetListView() {
        return categorySetListView;
    }

    @GlobalCommand
    @NotifyChange({ "tagSubjectAction", "categorySetListView" })
    public void show(@BindingParam("trigger") TriggerView trigger, @BindingParam("action") ActionType action) {
        if (ActionType.TAG_SUBJECT.equals(action)) {
            tagSubjectAction = new TagSubjectActionView();
            List<SubjectCategorySet> subjectCategoriesSet = new ArrayList<>();
            for (TriggerSubjectCategorySetView subjectCategoryView : trigger.getSubjectCategorySets()) {
                SubjectCategorySet subjectCategorySet = new SubjectCategorySet();
                subjectCategorySet.setId(subjectCategoryView.getSubjectCategorySet().getId());
                subjectCategorySet.setDescription(subjectCategoryView.getSubjectCategorySet().getDescription());
                subjectCategorySet.setCategories(subjectCategoryView.getSubjectCategorySet().getCategories());
                subjectCategoriesSet.add(subjectCategorySet);
            }
            categorySetListView.setList(subjectCategoriesSet);
            tagSubjectAction.setCategorySets(new ArrayList<>());
            categorySetListView.setFilteredList(new ArrayList<>(categorySetListView.getList()));
            categorySetListView.setEnabledAdd(!categorySetListView.getFilteredList().isEmpty());
        }
    }

    @Command
    public void save() {
        AutomaticActionCriteria actionCriteria = new AutomaticActionCriteria();
        actionCriteria.setActionType(ActionType.TAG_SUBJECT);
        Map<ActionParameter, Object> parameters = new HashMap<>();
        List<TagSubjectCategorySetView> categorySets = tagSubjectAction.getCategorySets();
        String[] categoriesSet = categorySets.stream().map(categorySet -> {
            return categorySet.getCategorySet().getId();
        }).toArray(size -> new String[size]);
        String[] categories = categorySets.stream().map(categorySet -> categorySet.getCategories())
                .flatMap(l -> l.stream()).map(SubjectCategory::getId).toArray(size -> new String[size]);
        parameters.put(ActionParameter.CATEGORIES_SET, categoriesSet);
        parameters.put(ActionParameter.CATEGORIES, categories);
        parameters.put(ActionParameter.COMMENT, buildComment(categorySets));
        actionCriteria.setParameters(parameters);

        HashMap<String, Object> args = new HashMap<>();
        args.put("actionCriteria", actionCriteria);
        BindUtils.postGlobalCommand(null, null, "saveActionCriteria", args);
    }

    private String buildComment(List<TagSubjectCategorySetView> categorySets) {
        return categorySets
                .stream()
                .map(categorySet -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append(categorySet.getCategorySet().getDescription());
                    sb.append("(");
                    sb.append(categorySet.getCategories().stream().map(SubjectCategory::getDescription)
                            .collect(Collectors.joining(", ")));
                    sb.append(")");
                    return sb.toString();
                }).collect(Collectors.joining(", "));
    }

    @Command
    @NotifyChange({ "categorySetListView" })
    public void addCategorySet(@BindingParam("fxTagSubjectAction") TagSubjectActionView fxTagSubjectAction) {
        TagSubjectCategorySetView categorySet = new TagSubjectCategorySetView();
        categorySet.setEditingStatus(Boolean.TRUE);
        fxTagSubjectAction.getCategorySets().add(categorySet);
        categorySetListView.setEnabledAdd(false);
        BindUtils.postNotifyChange(null, null, fxTagSubjectAction, "categorySets");
    }

    @Command
    @NotifyChange({ "categorySetListView" })
    public void editCategorySet(@BindingParam("index") int idx,
            @BindingParam("fxTagSubjectAction") TagSubjectActionView fxTagSubjectAction) {
        TagSubjectCategorySetView subjectCategorySet = fxTagSubjectAction.getCategorySets().get(idx);
        subjectCategorySet.setEditingStatus(Boolean.TRUE);
        addSubjectCategorySetFilteredList(categorySetListView, subjectCategorySet.getCategorySet());
        categorySetListView.setEnabledAdd(false);
        BindUtils.postNotifyChange(null, null, fxTagSubjectAction, "categorySets");
    }

    @Command
    @NotifyChange({ "categorySetListView" })
    public void confirmCategorySet(@BindingParam("index") int idx,
            @BindingParam("fxTagSubjectAction") TagSubjectActionView fxTagSubjectAction) {
        TagSubjectCategorySetView subjectCategorySet = fxTagSubjectAction.getCategorySets().get(idx);
        subjectCategorySet.setEditingStatus(Boolean.FALSE);
        deleteSubjectCategorySetFilteredList(categorySetListView, subjectCategorySet.getCategorySet());
        categorySetListView.setEnabledAdd(!categorySetListView.getFilteredList().isEmpty());
        BindUtils.postNotifyChange(null, null, fxTagSubjectAction, "categorySets");

    }

    @Command
    @NotifyChange({ "categorySetListView" })
    public void deleteCategorySet(@BindingParam("index") int idx,
            @BindingParam("fxTagSubjectAction") TagSubjectActionView fxTagSubjectAction) {
        TagSubjectCategorySetView deletedSubjectCategorySet = fxTagSubjectAction.getCategorySets().remove(idx);
        if (!deletedSubjectCategorySet.isEditingStatus()) {
            addSubjectCategorySetFilteredList(categorySetListView, deletedSubjectCategorySet.getCategorySet());
        }
        categorySetListView.setEnabledAdd(true);
        for (TagSubjectCategorySetView categorySet : fxTagSubjectAction.getCategorySets()) {
            if (categorySet.isEditingStatus()) {
                categorySetListView.setEnabledAdd(false);
            }
        }
        BindUtils.postNotifyChange(null, null, fxTagSubjectAction, "categorySets");

    }

    @Command
    public void selectCategorySet(@BindingParam("index") int idx,
            @BindingParam("fxTagSubjectAction") TagSubjectActionView fxTagSubjectAction) {
        TagSubjectCategorySetView subjectCategorySet = fxTagSubjectAction.getCategorySets().get(idx);
        if (subjectCategorySet.getCategories() != null) {
            subjectCategorySet.getCategories().clear();
        } else {
            subjectCategorySet.setCategories(new ArrayList<>());
        }
        Map<String, Object> args = new HashMap<>();
        args.put("subjectCategorySet", subjectCategorySet);
        Executions.createComponents("/pages/cases/actions/choose-subject-categories.zul", null, args);
    }

    @Command
    public void editCategories(@BindingParam("index") int idx,
            @BindingParam("fxTagSubjectAction") TagSubjectActionView fxTagSubjectAction) {
        TagSubjectCategorySetView subjectCategorySet = fxTagSubjectAction.getCategorySets().get(idx);
        Map<String, Object> args = new HashMap<>();
        args.put("subjectCategorySet", subjectCategorySet);
        Executions.createComponents("/pages/cases/actions/choose-subject-categories.zul", null, args);
    }

    @Command
    public void removeCategory(@BindingParam("categorySetIndex") int categorySetIndex,
            @BindingParam("category") SubjectCategory category,
            @BindingParam("fxTagSubjectAction") TagSubjectActionView fxTagSubjectAction) {
        TagSubjectCategorySetView subjectCategorySet = fxTagSubjectAction.getCategorySets().get(categorySetIndex);
        subjectCategorySet.getCategories().remove(category);
        BindUtils.postNotifyChange(null, null, subjectCategorySet, "categories");
    }

    private void addSubjectCategorySetFilteredList(ListView<SubjectCategorySet> categorySetListView,
            SubjectCategorySet subjectCategorySet) {
        if (subjectCategorySet == null) {
            return;
        }
        for (SubjectCategorySet subjectCategorySetList : categorySetListView.getList()) {
            if (subjectCategorySetList.getId().equals(subjectCategorySet.getId())) {
                categorySetListView.getFilteredList().add(subjectCategorySetList);
            }
        }
    }

    private void deleteSubjectCategorySetFilteredList(ListView<SubjectCategorySet> categorySetListView,
            SubjectCategorySet subjectCategorySet) {
        if (subjectCategorySet == null) {
            return;
        }
        categorySetListView.getFilteredList().remove(subjectCategorySet);
    }

}
