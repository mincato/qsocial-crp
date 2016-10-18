package com.qsocialnow.viewmodel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Filedownload;

import com.qsocialnow.common.model.cases.CaseListView;
import com.qsocialnow.common.model.cases.SubjectListView;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.CaseService;
import com.qsocialnow.services.SubjectService;

@VariableResolver(DelegatingVariableResolver.class)
public class CasesViewModel implements Serializable {

    private static final long serialVersionUID = 2259179419421396093L;

    private static final int PAGE_SIZE_DEFAULT = 15;

    private static final int ACTIVE_PAGE_DEFAULT = 0;

    private int pageSize = PAGE_SIZE_DEFAULT;

    private int activePage = ACTIVE_PAGE_DEFAULT;

    private String sortField = "openDate";

    private boolean sortOrder = false;

    @WireVariable
    private CaseService caseService;
    
    @WireVariable
    private SubjectService subjectService;

    private boolean moreResults;

    private List<CaseListView> cases = new ArrayList<>();

    // filters
    private boolean filterActive;

    private List<String> subjectsFilterOptions = new ArrayList<>();

    private Long fromDate;

    private Long toDate;

    private String title;

    private String description;

    private String subject;

    private Boolean isPendingResponse;

    @Init
    public void init() {
        this.subjectsFilterOptions = getSubjects();
        this.filterActive = false;
        findCases();
    }

    private List<String> getSubjects() {

        return null;
    }

    public List<CaseListView> getCases() {
        return this.cases;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public boolean isMoreResults() {
        return moreResults;
    }

    @Command
    @NotifyChange({ "cases", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findCases();
    }

    @Command
    @NotifyChange({ "cases", "moreResults" })
    public void sortList(@BindingParam("sortField") String sortField) {
        this.sortField = sortField;
        this.sortOrder = !this.sortOrder;
        this.activePage = 0;
        this.cases.clear();
        this.findCases();
    }

    private PageResponse<CaseListView> findCases() {
        PageRequest pageRequest = new PageRequest(activePage, pageSize, sortField);
        pageRequest.setSortOrder(this.sortOrder);

        PageResponse<CaseListView> pageResponse = caseService.findAll(pageRequest, getFilters());
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            this.cases.addAll(pageResponse.getItems());
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }

        return pageResponse;
    }

    @Command
    @NotifyChange({ "cases", "moreResults", "filterActive" })
    public void search() {
        this.filterActive = true;
        this.setDefaultPage();
        this.cases.clear();
        this.findCases();
    }
    
    @Command
    public void download() {
    	byte[] data = caseService.getReport(null);
    		Filedownload.save(data, "application/vnd.ms-excel", "file.xls");
		
    	
    }
    
    

    private Map<String, String> getFilters() {
        if (!filterActive) {
            return null;
        }

        Map<String, String> filters = new HashMap<String, String>();
        if (this.title != null && !this.title.isEmpty()) {
            filters.put("title", this.title);
        }

        if (this.description != null && !this.description.isEmpty()) {
            filters.put("description", this.description);
        }

        if (this.isPendingResponse != null) {
            filters.put("pendingResponse", String.valueOf(this.isPendingResponse));
        }

        if (this.fromDate != null) {
            filters.put("fromOpenDate", String.valueOf(this.fromDate));
        }

        if (this.toDate != null) {
            filters.put("toOpenDate", String.valueOf(this.toDate));
        }

        return filters;
    }

    private void setDefaultPage() {
        this.pageSize = PAGE_SIZE_DEFAULT;
        this.activePage = ACTIVE_PAGE_DEFAULT;
    }

    public List<String> getSubjectsFilterOptions() {
        return subjectsFilterOptions;
    }

    public void setSubjectsFilterOptions(List<String> subjectsFilterOptions) {
        this.subjectsFilterOptions = subjectsFilterOptions;
    }

    public boolean isFilterActive() {
        return filterActive;
    }

    public void setFilterActive(boolean filterActive) {
        this.filterActive = filterActive;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isPendingResponse() {
        return isPendingResponse;
    }

    public void setPendingResponse(boolean isPendingResponse) {
        this.isPendingResponse = isPendingResponse;
    }

}
