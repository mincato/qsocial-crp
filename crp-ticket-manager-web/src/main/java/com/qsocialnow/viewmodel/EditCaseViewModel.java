package com.qsocialnow.viewmodel;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Filedownload;

import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.RegistryListView;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.CaseCategory;
import com.qsocialnow.common.model.config.CaseCategorySet;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.SubjectCategory;
import com.qsocialnow.common.model.config.SubjectCategorySet;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.services.ActionRegistryService;
import com.qsocialnow.services.CaseCategorySetService;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.FileService;
import com.qsocialnow.services.TriggerService;
import com.qsocialnow.util.DeleteOnCloseInputStream;

@VariableResolver(DelegatingVariableResolver.class)
public class EditCaseViewModel implements Serializable {

    private static final String ALL_VALUES = "ALL_VALUES";

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private CaseService caseService;

    @WireVariable
    private TriggerService triggerService;

    @WireVariable
    private ActionRegistryService actionRegistryService;

    @WireVariable
    private CaseCategorySetService caseCategorySetService;

    @WireVariable
    private FileService fileService;

    private EditCaseView currentCase;

    private static final int PAGE_SIZE_DEFAULT = 15;

    private static final int ACTIVE_PAGE_DEFAULT = 0;

    private int pageSize = PAGE_SIZE_DEFAULT;

    private int activePage = ACTIVE_PAGE_DEFAULT;

    private boolean filterActive;

    private String caseId;

    private boolean moreResults;

    private String keyword;

    private String action;

    private String user;

    private Date fromDate;

    private Date toDate;

    private List<RegistryListView> registries = new ArrayList<>();

    private List<ActionType> actionOptions = new ArrayList<>();

    private List<String> actionFilterOptions = new ArrayList<>();

    private ActionType selectedAction;

    @Init
    public void init(@QueryParam("case") String caseSelected) {
        this.caseId = caseSelected;
        this.currentCase = new EditCaseView();

        this.actionFilterOptions.add(0, ALL_VALUES);
        for (int i = 0; i < ActionType.values().length; i++) {
            this.actionFilterOptions.add(ActionType.values()[i].toString());
        }
        this.action = ALL_VALUES;
        findCase(this.caseId);
        findRegistriesBy();
        initCaseCategories();
        initCategoriesForSubject();
        this.actionOptions = getAllowedActionsByCase();
    }

    private void initCaseCategories() {
        if (currentCase.getTriggerCategories() == null) {
            currentCase.setTriggerCategories(triggerService.findCategories(currentCase.getCaseObject().getDomainId(),
                    currentCase.getCaseObject().getTriggerId()));
        }
        initCaseCategoriesSet();
        initCategories();

    }

    private void initCaseCategoriesSet() {
        List<CaseCategorySet> categoriesSet;
        Set<String> caseCategoriesSet = currentCase.getCaseObject().getCaseCategoriesSet();
        if (CollectionUtils.isNotEmpty(caseCategoriesSet)) {
            categoriesSet = currentCase.getTriggerCategories().stream()
                    .filter(caseCategorySet -> caseCategoriesSet.contains(caseCategorySet.getId()))
                    .collect(Collectors.toList());

        } else {
            categoriesSet = new ArrayList<>();
        }
        currentCase.setCaseCategoriesSet(categoriesSet);

    }

    private void initCategories() {
        List<CaseCategory> categories;
        Set<String> caseCategories = currentCase.getCaseObject().getCaseCategories();
        if (CollectionUtils.isNotEmpty(caseCategories)) {
            Stream<CaseCategory> caseCategoriesStream = currentCase.getTriggerCategories().stream()
                    .map(categorySet -> categorySet.getCategories()).flatMap(l -> l.stream());
            categories = caseCategoriesStream.filter(caseCategory -> caseCategories.contains(caseCategory.getId()))
                    .collect(Collectors.toList());
        } else {
            categories = new ArrayList<>();
        }
        currentCase.setCaseCategories(categories);
    }

    private void initCategoriesForSubject() {
        if (currentCase.getTriggerSubjectCategories() == null) {
            currentCase.setTriggerSubjectCategories(triggerService.findSubjectCategories(currentCase.getCaseObject()
                    .getDomainId(), currentCase.getCaseObject().getTriggerId()));
        }
        initSubjectCategoriesSet();
        initSubjectCategories();

    }

    private void initSubjectCategoriesSet() {
        List<SubjectCategorySet> categoriesSet;
        Set<String> subjectCategoriesSet = currentCase.getCaseObject().getSubject().getSubjectCategorySet();
        if (CollectionUtils.isNotEmpty(subjectCategoriesSet)) {
            categoriesSet = currentCase.getTriggerSubjectCategories().stream()
                    .filter(subjectCategorySet -> subjectCategoriesSet.contains(subjectCategorySet.getId()))
                    .collect(Collectors.toList());

        } else {
            categoriesSet = new ArrayList<>();
        }
        currentCase.setSubjectCategoriesSet(categoriesSet);

    }

    private void initSubjectCategories() {
        List<SubjectCategory> categories;
        Set<String> subjectCategories = currentCase.getCaseObject().getSubject().getSubjectCategory();
        if (CollectionUtils.isNotEmpty(subjectCategories)) {
            Stream<SubjectCategory> subjectCategoriesStream = currentCase.getTriggerSubjectCategories().stream()
                    .map(categorySet -> categorySet.getCategories()).flatMap(l -> l.stream());
            categories = subjectCategoriesStream.filter(
                    subjectCategory -> subjectCategories.contains(subjectCategory.getId()))
                    .collect(Collectors.toList());
        } else {
            categories = new ArrayList<>();
        }
        currentCase.setSubjectCategories(categories);
    }

    @Command
    @NotifyChange({ "registries", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findRegistriesBy();
    }

    @Command
    @NotifyChange("currentCase")
    public void clear() {

    }

    private void findCase(String caseSelected) {
        this.currentCase.setCaseObject(caseService.findById(caseSelected));
        this.currentCase.setSegment(triggerService.findSegment(this.currentCase.getCaseObject().getDomainId(),
                this.currentCase.getCaseObject().getTriggerId(), this.currentCase.getCaseObject().getSegmentId()));
        this.currentCase.setTrigger(triggerService.findOne(this.currentCase.getCaseObject().getDomainId(),
                this.currentCase.getCaseObject().getTriggerId()));
        this.currentCase.setSource(Media.getByValue(currentCase.getCaseObject().getSource()));
    }

    private List<ActionType> getAllowedActionsByCase() {
        if (currentCase.getCaseObject() != null)
            return currentCase.getCaseObject().getAllowedManualActions();
        else
            return new ArrayList<ActionType>();
    }

    @Command
    @NotifyChange({ "registries", "moreResults", "filterActive" })
    public void search() {
        this.filterActive = true;
        this.setDefaultPage();
        this.registries.clear();
        this.findRegistriesBy();
    }

    @Command
    @NotifyChange({ "currentCase" })
    public void save() {

    }

    private void refreshRegistries() {
        this.registries.clear();
        this.filterActive = false;
        this.action = null;
        this.fromDate = null;
        this.toDate = null;
        this.user = null;
        this.keyword = null;
        this.setDefaultPage();
        this.actionOptions = getAllowedActionsByCase();
        this.findRegistriesBy();
    }

    @GlobalCommand
    @NotifyChange({ "currentCase", "selectedAction", "registries", "moreResults", "actionOptions" })
    public void actionExecuted(@BindingParam("caseUpdated") Case caseUpdated) {
        this.currentCase.setCaseObject(caseUpdated);
        this.selectedAction = null;
        this.refreshRegistries();
        this.initCaseCategories();
        this.initCategoriesForSubject();
    }

    @Command
    public void onSelectAction() {
        Map<String, Object> args = new HashMap<>();
        args.put("currentCase", currentCase);
        args.put("action", selectedAction);
        BindUtils.postGlobalCommand(null, null, "show", args);
    }

    @Command
    public void downloadAttachment(@BindingParam("attachment") String attachment) throws Exception {
        File file = fileService.download(attachment, currentCase.getCaseObject());
        Filedownload.save(new DeleteOnCloseInputStream(file), null, attachment);
    }

    private PageResponse<RegistryListView> findRegistriesBy() {
        String actionValue = ALL_VALUES.equals(this.action) ? null : this.action;
        PageResponse<RegistryListView> pageResponse = actionRegistryService.findRegistriesBy(activePage, pageSize,
                this.caseId, this.keyword, actionValue, this.user, this.fromDate, this.toDate);

        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.registries.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }
        return pageResponse;
    }

    private void setDefaultPage() {
        this.pageSize = PAGE_SIZE_DEFAULT;
        this.activePage = ACTIVE_PAGE_DEFAULT;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<RegistryListView> getRegistries() {
        return registries;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public boolean isMoreResults() {
        return moreResults;
    }

    public List<ActionType> getActionOptions() {
        return actionOptions;
    }

    public ActionType getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(ActionType selectedAction) {
        this.selectedAction = selectedAction;
    }

    public EditCaseView getCurrentCase() {
        return currentCase;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public List<String> getActionFilterOptions() {
        return actionFilterOptions;
    }

    public boolean isFilterActive() {
        return filterActive;
    }
}
