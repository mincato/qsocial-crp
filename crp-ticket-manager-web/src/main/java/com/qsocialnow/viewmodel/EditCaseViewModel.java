package com.qsocialnow.viewmodel;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.annotation.QueryParam;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Filedownload;

import com.qsocialnow.common.model.cases.ActionRegistryStatus;
import com.qsocialnow.common.model.cases.Case;
import com.qsocialnow.common.model.cases.ErrorType;
import com.qsocialnow.common.model.cases.RegistryListView;
import com.qsocialnow.common.model.config.ActionType;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.config.Resolution;
import com.qsocialnow.common.model.config.Team;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.converters.DateConverter;
import com.qsocialnow.model.EditCaseView;
import com.qsocialnow.services.ActionRegistryService;
import com.qsocialnow.services.CaseCategorySetService;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.FileService;
import com.qsocialnow.services.ResolutionService;
import com.qsocialnow.services.SubjectCategorySetService;
import com.qsocialnow.services.TeamService;
import com.qsocialnow.services.TriggerService;
import com.qsocialnow.services.UserSessionService;
import com.qsocialnow.util.DeleteOnCloseInputStream;
import com.qsocialnow.viewmodel.factory.EditCaseCaseCategoriesFactory;
import com.qsocialnow.viewmodel.factory.EditCaseSubjectCategoriesFactory;

@VariableResolver(DelegatingVariableResolver.class)
public class EditCaseViewModel implements Serializable {

    private static final String ALL_VALUES = "ALL_VALUES";

    private static final long serialVersionUID = 2259179419421396093L;

    @WireVariable
    private CaseService caseService;

    @WireVariable
    private TriggerService triggerService;

    @WireVariable
    private CaseCategorySetService caseCategorySetService;

    @WireVariable
    private SubjectCategorySetService subjectCategorySetService;

    @WireVariable
    private ActionRegistryService actionRegistryService;

    @WireVariable
    private ResolutionService resolutionService;

    @WireVariable
    private TeamService teamService;

    @WireVariable
    private FileService fileService;

    @WireVariable
    private UserSessionService userSessionService;

    private EditCaseView currentCase;

    private DateConverter dateConverter;

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

    private Long fromDate;

    private Long toDate;

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
        initOpenCaseDeepLinkUrl();
        initResolution();
        initCaseCategories();
        initCategoriesForSubject();
        initTeam();
        this.actionOptions = getAllowedActionsByCase();
        this.dateConverter = new DateConverter(userSessionService.getTimeZone());
    }

    private void initResolution() {
        if (currentCase.getCaseObject().getResolution() != null) {
            Resolution resolution = resolutionService.findOne(currentCase.getCaseObject().getDomainId(), currentCase
                    .getCaseObject().getResolution());
            if (resolution != null) {
                currentCase.setResolutionDescription(resolution.getDescription());
            }
        }
    }

    private void initTeam() {
        if (currentCase.getCaseObject().getTeamId() != null) {
            Team team = teamService.findOne(currentCase.getCaseObject().getTeamId());
            if (team != null) {
                currentCase.setTeamDescription(team.getName());
            }

        }
    }

    private void initOpenCaseDeepLinkUrl() {
        if (!CollectionUtils.isEmpty(registries)) {
            for (int i = 0; i < registries.size(); i++) {
                if (ActionType.OPEN_CASE.name().equals(registries.get(i).getAction())) {
                    currentCase.setOpenCaseDeepLinkUrl(registries.get(i).getDeepLink());
                }
            }
        }
    }

    private void initCaseCategories() {
        EditCaseCaseCategoriesFactory factory = new EditCaseCaseCategoriesFactory();
        factory.setCaseCategorySetService(caseCategorySetService);
        factory.setTriggerService(triggerService);
        factory.init(currentCase);
    }

    private void initCategoriesForSubject() {
        EditCaseSubjectCategoriesFactory factory = new EditCaseSubjectCategoriesFactory();
        factory.setSubjectCategorySetService(subjectCategorySetService);
        factory.setTriggerService(triggerService);
        factory.init(currentCase);
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
        this.currentCase.setSource(Media.getByValue(currentCase.getCaseObject().getSource()));
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

    @Command
    public String createRegistryStatusMessage(RegistryListView registry) {
        if (registry.getStatus() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("cases.registry.status.");
            sb.append(registry.getStatus().name());
            if (ActionRegistryStatus.ERROR.equals(registry.getStatus())) {
                ErrorType errorType = registry.getErrorType() != null ? registry.getErrorType() : ErrorType.UNKNOWN;
                sb.append(".");
                sb.append(errorType.name());
            }
            return Labels.getLabel(sb.toString());
        }
        return "";
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

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public List<String> getActionFilterOptions() {
        return actionFilterOptions;
    }

    public boolean isFilterActive() {
        return filterActive;
    }

    public DateConverter getDateConverter() {
        return dateConverter;
    }
}
