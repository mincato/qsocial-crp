package com.qsocialnow.viewmodel;

import java.io.Serializable;
import java.util.ArrayList;
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
import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.model.ListView;
import com.qsocialnow.model.TagCaseActionView;
import com.qsocialnow.model.TagCaseCategorySetView;
import com.qsocialnow.services.CaseService;

@VariableResolver(DelegatingVariableResolver.class)
public class TagCaseActionViewModel implements Serializable {

    private static final long serialVersionUID = 3231836966780468455L;

    @WireVariable
    private CaseService caseService;

    private String caseId;

    private TagCaseActionView tagCaseAction;

    private ListView<CaseCategorySet> categorySetListView;

    @Init
    public void init(@QueryParam("case") String caseId) {
        this.caseId = caseId;
        this.categorySetListView = new ListView<>();
        tagCaseAction = new TagCaseActionView();
    }

    public TagCaseActionView getTagCaseAction() {
        return tagCaseAction;
    }

    public void setTagCaseAction(TagCaseActionView tagCaseAction) {
        this.tagCaseAction = tagCaseAction;
    }

    public ListView<CaseCategorySet> getCategorySetListView() {
        return categorySetListView;
    }

    @GlobalCommand
    @NotifyChange({ "tagCaseAction", "categorySetListView" })
    public void show(@BindingParam("currentCase") EditCaseView currentCase, @BindingParam("action") ActionType action) {
        if (ActionType.TAG_CASE.equals(action)) {
            tagCaseAction = new TagCaseActionView();
            if (categorySetListView.getList() == null) {
                categorySetListView.setList(currentCase.getTriggerCategories());
            }
            tagCaseAction.setCategorySets(currentCase
                    .getCaseCategoriesSet()
                    .stream()
                    .map(caseCategorySet -> {
                        TagCaseCategorySetView tagCaseCategorySet = new TagCaseCategorySetView();
                        tagCaseCategorySet.setCategorySet(caseCategorySet);
                        tagCaseCategorySet.setCategories(caseCategorySet
                                .getCategories()
                                .stream()
                                .filter(category -> currentCase.getCaseObject().getCaseCategories()
                                        .contains(category.getId())).collect(Collectors.toList()));
                        tagCaseCategorySet.setEditingStatus(false);
                        return tagCaseCategorySet;
                    }).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(currentCase.getCaseObject().getCaseCategoriesSet())) {
                categorySetListView.setFilteredList(categorySetListView
                        .getList()
                        .stream()
                        .filter(categorySet -> !currentCase.getCaseObject().getCaseCategoriesSet()
                                .contains(categorySet.getId())).collect(Collectors.toList()));
            } else {
                categorySetListView.setFilteredList(new ArrayList<>(categorySetListView.getList()));
            }
            categorySetListView.setEnabledAdd(!categorySetListView.getFilteredList().isEmpty());
        }
    }

    @Command
    public void execute() {
        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setActionType(ActionType.TAG_CASE);
        Map<ActionParameter, Object> parameters = new HashMap<>();
        List<TagCaseCategorySetView> categorySets = tagCaseAction.getCategorySets();
        String[] categoriesSet = categorySets.stream().map(categorySet -> categorySet.getCategorySet().getId())
                .toArray(size -> new String[size]);
        String[] categories = categorySets.stream().map(categorySet -> categorySet.getCategories())
                .flatMap(l -> l.stream()).map(CaseCategory::getId).toArray(size -> new String[size]);
        parameters.put(ActionParameter.CATEGORIES_SET, categoriesSet);
        parameters.put(ActionParameter.CATEGORIES, categories);
        parameters.put(ActionParameter.COMMENT, buildComment(categorySets));
        actionRequest.setParameters(parameters);
        Case caseUpdated = caseService.executeAction(caseId, actionRequest);

        HashMap<String, Object> args = new HashMap<>();
        args.put("caseUpdated", caseUpdated);
        BindUtils.postGlobalCommand(null, null, "actionExecuted", args);
    }

    private String buildComment(List<TagCaseCategorySetView> categorySets) {
        return categorySets
                .stream()
                .map(categorySet -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append(categorySet.getCategorySet().getDescription());
                    sb.append("(");
                    sb.append(categorySet.getCategories().stream().map(CaseCategory::getDescription)
                            .collect(Collectors.joining(", ")));
                    sb.append(")");
                    return sb.toString();
                }).collect(Collectors.joining(", "));
    }

    @Command
    @NotifyChange({ "categorySetListView" })
    public void addCategorySet(@BindingParam("fxTagCaseAction") TagCaseActionView fxTagCaseAction) {
        TagCaseCategorySetView categorySet = new TagCaseCategorySetView();
        categorySet.setEditingStatus(Boolean.TRUE);
        fxTagCaseAction.getCategorySets().add(categorySet);
        categorySetListView.setEnabledAdd(false);
        BindUtils.postNotifyChange(null, null, fxTagCaseAction, "categorySets");
    }

    @Command
    @NotifyChange({ "categorySetListView" })
    public void editCategorySet(@BindingParam("index") int idx,
            @BindingParam("fxTagCaseAction") TagCaseActionView fxTagCaseAction) {
        TagCaseCategorySetView caseCategorySet = fxTagCaseAction.getCategorySets().get(idx);
        caseCategorySet.setEditingStatus(Boolean.TRUE);
        addCaseCategorySetFilteredList(categorySetListView, caseCategorySet.getCategorySet());
        categorySetListView.setEnabledAdd(false);
        BindUtils.postNotifyChange(null, null, fxTagCaseAction, "categorySets");
    }

    @Command
    @NotifyChange({ "categorySetListView" })
    public void confirmCategorySet(@BindingParam("index") int idx,
            @BindingParam("fxTagCaseAction") TagCaseActionView fxTagCaseAction) {
        TagCaseCategorySetView caseCategorySet = fxTagCaseAction.getCategorySets().get(idx);
        caseCategorySet.setEditingStatus(Boolean.FALSE);
        deleteCaseCategorySetFilteredList(categorySetListView, caseCategorySet.getCategorySet());
        categorySetListView.setEnabledAdd(!categorySetListView.getFilteredList().isEmpty());
        BindUtils.postNotifyChange(null, null, fxTagCaseAction, "categorySets");

    }

    @Command
    @NotifyChange({ "categorySetListView" })
    public void deleteCategorySet(@BindingParam("index") int idx,
            @BindingParam("fxTagCaseAction") TagCaseActionView fxTagCaseAction) {
        TagCaseCategorySetView deletedCaseCategorySet = fxTagCaseAction.getCategorySets().remove(idx);
        if (!deletedCaseCategorySet.isEditingStatus()) {
            addCaseCategorySetFilteredList(categorySetListView, deletedCaseCategorySet.getCategorySet());
        }
        categorySetListView.setEnabledAdd(true);
        for (TagCaseCategorySetView categorySet : fxTagCaseAction.getCategorySets()) {
            if (categorySet.isEditingStatus()) {
                categorySetListView.setEnabledAdd(false);
            }
        }
        BindUtils.postNotifyChange(null, null, fxTagCaseAction, "categorySets");

    }

    @Command
    public void selectCategorySet(@BindingParam("index") int idx,
            @BindingParam("fxTagCaseAction") TagCaseActionView fxTagCaseAction) {
        TagCaseCategorySetView caseCategorySet = fxTagCaseAction.getCategorySets().get(idx);
        if (caseCategorySet.getCategories() != null) {
            caseCategorySet.getCategories().clear();
        } else {
            caseCategorySet.setCategories(new ArrayList<>());
        }
        Map<String, Object> args = new HashMap<>();
        args.put("caseCategorySet", caseCategorySet);
        Executions.createComponents("/pages/cases/actions/choose-categories.zul", null, args);
    }

    @Command
    public void editCategories(@BindingParam("index") int idx,
            @BindingParam("fxTagCaseAction") TagCaseActionView fxTagCaseAction) {
        TagCaseCategorySetView caseCategorySet = fxTagCaseAction.getCategorySets().get(idx);
        Map<String, Object> args = new HashMap<>();
        args.put("caseCategorySet", caseCategorySet);
        Executions.createComponents("/pages/cases/actions/choose-categories.zul", null, args);
    }

    @Command
    public void removeCategory(@BindingParam("categorySetIndex") int categorySetIndex,
            @BindingParam("category") CaseCategory category,
            @BindingParam("fxTagCaseAction") TagCaseActionView fxTagCaseAction) {
        TagCaseCategorySetView caseCategorySet = fxTagCaseAction.getCategorySets().get(categorySetIndex);
        caseCategorySet.getCategories().remove(category);
        BindUtils.postNotifyChange(null, null, caseCategorySet, "categories");
    }

    private void addCaseCategorySetFilteredList(ListView<CaseCategorySet> categorySetListView,
            CaseCategorySet caseCategorySet) {
        if (caseCategorySet == null) {
            return;
        }
        for (CaseCategorySet caseCategorySetList : categorySetListView.getList()) {
            if (caseCategorySetList.getId().equals(caseCategorySet.getId())) {
                categorySetListView.getFilteredList().add(caseCategorySetList);
            }
        }
    }

    private void deleteCaseCategorySetFilteredList(ListView<CaseCategorySet> categorySetListView,
            CaseCategorySet caseCategorySet) {
        if (caseCategorySet == null) {
            return;
        }
        categorySetListView.getFilteredList().remove(caseCategorySet);
    }

}
