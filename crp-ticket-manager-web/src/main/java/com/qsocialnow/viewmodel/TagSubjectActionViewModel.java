package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.ActionParameter;
import com.qsocialnow.common.model.cases.ActionRequest;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.model.ListView;
import com.qsocialnow.model.TagSubjectActionView;
import com.qsocialnow.model.TagSubjectCategorySetView;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.SubjectCategorySetService;

@VariableResolver(DelegatingVariableResolver.class)
public class TagSubjectActionViewModel implements Serializable {

    private static final long serialVersionUID = 992054010762951903L;

    @WireVariable
    private CaseService caseService;

    @WireVariable
    private SubjectCategorySetService subjectCategorySetService;

    private String caseId;

    private TagSubjectActionView tagSubjectAction;

    private ListView<SubjectCategorySet> categorySetListView;

    @Init
    public void init(@QueryParam("case") String caseId) {
        this.caseId = caseId;
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
    public void show(@BindingParam("currentCase") EditCaseView currentCase, @BindingParam("action") ActionType action) {
        if (ActionType.TAG_SUBJECT.equals(action)) {
            tagSubjectAction = new TagSubjectActionView();
            if (categorySetListView.getList() == null) {
                categorySetListView.setList(currentCase.getTriggerSubjectCategories());
            }
            tagSubjectAction.setCategorySets(currentCase
                    .getSubjectCategoriesSet()
                    .stream()
                    .map(subjectCategorySet -> {
                        TagSubjectCategorySetView tagSubjectCategorySet = new TagSubjectCategorySetView();
                        tagSubjectCategorySet.setCategorySet(subjectCategorySet);
                        tagSubjectCategorySet.setCategories(subjectCategorySet
                                .getCategories()
                                .stream()
                                .filter(category -> currentCase.getCaseObject().getSubject().getSubjectCategory()
                                        .contains(category.getId())).collect(Collectors.toList()));

                        tagSubjectCategorySet.setEditingStatus(false);
                        return tagSubjectCategorySet;
                    }).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(currentCase.getCaseObject().getSubject().getSubjectCategorySet())) {
                categorySetListView.setFilteredList(categorySetListView
                        .getList()
                        .stream()
                        .filter(categorySet -> !currentCase.getCaseObject().getSubject().getSubjectCategorySet()
                                .contains(categorySet.getId())).collect(Collectors.toList()));
            } else {
                categorySetListView.setFilteredList(new ArrayList<>(categorySetListView.getList()));
            }
            tagSubjectAction.setPreviousConfiguration(getComparableTuples(tagSubjectAction.getCategorySets()));
            categorySetListView.setEnabledAdd(!categorySetListView.getFilteredList().isEmpty());
        }
    }

    private List<String[]> getComparableTuples(List<TagSubjectCategorySetView> list) {
        List<String[]> tuples = new ArrayList<String[]>();
        for (TagSubjectCategorySetView tagSubjectCategorySetView : list) {
            for (SubjectCategory category : tagSubjectCategorySetView.getCategories()) {
                tuples.add(new String[] { tagSubjectCategorySetView.getCategorySet().getId(), category.getId() });
            }
        }
        return tuples;
    }

    @Command
    public void execute() {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.TAG_SUBJECT);
        Map<ActionParameter, Object> parameters = new HashMap<>();
        List<TagSubjectCategorySetView> categorySets = tagSubjectAction.getCategorySets();
        List<String[]> modified = getComparableTuples(tagSubjectAction.getCategorySets());
        List<String[]> removed = tagSubjectAction.getPreviousConfiguration().stream().filter(tuple -> {
            return modified.stream().anyMatch(pTuple -> Arrays.equals(pTuple, tuple));
        }).collect(Collectors.toList());
        List<String[]> added = modified
                .stream()
                .filter(tuple -> {
                    return !tagSubjectAction.getPreviousConfiguration().stream()
                            .anyMatch(pTuple -> Arrays.equals(pTuple, tuple));
                }).collect(Collectors.toList());
        parameters.put(ActionParameter.CATEGORIES_ADDED, added);
        parameters.put(ActionParameter.CATEGORIES_REMOVED, removed);
        parameters.put(ActionParameter.COMMENT, buildComment(categorySets));
        actionRequest.setParameters(parameters);
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);

        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
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
