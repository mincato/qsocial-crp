package com.qsocialnow.viewmodel.subjectcategoryset;

import static com.qsocialnow.pagination.PaginationConstants.ACTIVE_PAGE_DEFAULT;
import static com.qsocialnow.pagination.PaginationConstants.PAGE_SIZE_DEFAULT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.SubjectCategorySetListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.SubjectCategorySetService;

@VariableResolver(DelegatingVariableResolver.class)
public class SubjectCategorySetsViewModel implements Serializable {

    private static final long serialVersionUID = -1023991338162415995L;

    @WireVariable
    private SubjectCategorySetService subjectCategorySetService;

    private boolean moreResults;

    private List<SubjectCategorySetListView> subjectCategorySets = new ArrayList<>();

    private int pageSize = PAGE_SIZE_DEFAULT;
    private int activePage = ACTIVE_PAGE_DEFAULT;

    private String keyword;

    private boolean filterActive = false;

    @Init
    public void init() {
        findSubjectCategorySets();
    }

    @Command
    @NotifyChange({ "subjectCategorySets", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findSubjectCategorySets();
    }

    private PageResponse<SubjectCategorySetListView> findSubjectCategorySets() {
        PageResponse<SubjectCategorySetListView> pageResponse = subjectCategorySetService.findAll(activePage, pageSize,
                getFilters());
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.subjectCategorySets.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }

        return pageResponse;
    }

    @Command
    @NotifyChange({ "subjectCategorySets", "moreResults", "filterActive" })
    public void search() {
        this.filterActive = !StringUtils.isBlank(this.keyword);
        this.setDefaultPage();
        this.subjectCategorySets.clear();
        this.findSubjectCategorySets();
    }

    @Command
    public void openEdit(@BindingParam("subjectcategoryset") String subjectCategorySetId) {
        Map<String, Object> arg = new HashMap<String, Object>();
        arg.put("subjectcategoryset", subjectCategorySetId);
        Executions.createComponents("/pages/subject-category-set/edit-subject-category-set.zul", null, arg);
        Clients.evalJavaScript("initSwitch()");
    }

    @GlobalCommand
    @NotifyChange("subjectCategorySets")
    public void changeSubjectCategorySet(
            @BindingParam("subjectCategorySetChanged") SubjectCategorySetView subjectCategorySetChanged) {
        if (subjectCategorySetChanged != null) {
            Optional<SubjectCategorySetListView> subjectCategorySetOptional = subjectCategorySets.stream()
                    .filter(subjectCategorySet -> subjectCategorySet.getId().equals(subjectCategorySetChanged.getId()))
                    .findFirst();
            if (subjectCategorySetOptional.isPresent()) {
                SubjectCategorySetListView subjectCategorySetListView = subjectCategorySetOptional.get();
                subjectCategorySetListView.setDescription(subjectCategorySetChanged.getDescription());
            }
        }
    }

    private Map<String, String> getFilters() {
        if (StringUtils.isBlank(this.keyword) || !this.filterActive) {
            return null;
        }
        Map<String, String> filters = new HashMap<String, String>();
        filters.put("description", this.keyword);
        return filters;
    }

    private void setDefaultPage() {
        this.pageSize = PAGE_SIZE_DEFAULT;
        this.activePage = ACTIVE_PAGE_DEFAULT;
    }

    public boolean isFilterActive() {
        return filterActive;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public boolean isMoreResults() {
        return moreResults;
    }

    public List<SubjectCategorySetListView> getSubjectCategorySets() {
        return subjectCategorySets;
    }

    public void setSubjectCategorySets(List<SubjectCategorySetListView> subjectCategorySets) {
        this.subjectCategorySets = subjectCategorySets;
    }

}