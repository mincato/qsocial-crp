package com.qsocialnow.viewmodel.casecategoryset;

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
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.config.CaseCategorySetListView;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.CaseCategorySetService;

@VariableResolver(DelegatingVariableResolver.class)
public class CaseCategorySetsViewModel implements Serializable {

    private static final long serialVersionUID = -1023991338162415995L;

    @WireVariable
    private CaseCategorySetService caseCategorySetService;

    private boolean moreResults;

    private List<CaseCategorySetListView> caseCategorySets = new ArrayList<>();

    private int pageSize = PAGE_SIZE_DEFAULT;
    private int activePage = ACTIVE_PAGE_DEFAULT;

    private String keyword;

    private boolean filterActive = false;

    @Init
    public void init() {
        findCaseCategorySets();
    }

    @Command
    @NotifyChange({ "caseCategorySets", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findCaseCategorySets();
    }

    private PageResponse<CaseCategorySetListView> findCaseCategorySets() {
        PageResponse<CaseCategorySetListView> pageResponse = caseCategorySetService.findAll(activePage, pageSize,
                getFilters());
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.caseCategorySets.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }

        return pageResponse;
    }

    @Command
    @NotifyChange({ "caseCategorySets", "moreResults", "filterActive" })
    public void search() {
        this.filterActive = !StringUtils.isBlank(this.keyword);
        this.setDefaultPage();
        this.caseCategorySets.clear();
        this.findCaseCategorySets();
    }

    @Command
    public void openEdit(@BindingParam("casecategoryset") String caseCategorySetId) {
        Map<String, Object> arg = new HashMap<String, Object>();
        arg.put("casecategoryset", caseCategorySetId);
        Executions.createComponents("/pages/case-category-set/edit-case-category-set.zul", null, arg);
    }

    @GlobalCommand
    @NotifyChange("caseCategorySets")
    public void changeCaseCategorySet(@BindingParam("caseCategorySetChanged") CaseCategorySetView caseCategorySetChanged) {
        if (caseCategorySetChanged != null) {
            Optional<CaseCategorySetListView> caseCategorySetOptional = caseCategorySets.stream()
                    .filter(caseCategorySet -> caseCategorySet.getId().equals(caseCategorySetChanged.getId()))
                    .findFirst();
            if (caseCategorySetOptional.isPresent()) {
                CaseCategorySetListView caseCategorySetListView = caseCategorySetOptional.get();
                caseCategorySetListView.setDescription(caseCategorySetChanged.getDescription());
                caseCategorySetListView.setActive(caseCategorySetChanged.getActive());
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

    public List<CaseCategorySetListView> getCaseCategorySets() {
        return caseCategorySets;
    }

    public void setCaseCategorySets(List<CaseCategorySetListView> caseCategorySets) {
        this.caseCategorySets = caseCategorySets;
    }

}