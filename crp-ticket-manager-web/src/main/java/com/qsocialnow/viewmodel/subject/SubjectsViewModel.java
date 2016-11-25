package com.qsocialnow.viewmodel.subject;

import static com.qsocialnow.pagination.PaginationConstants.ACTIVE_PAGE_DEFAULT;
import static com.qsocialnow.pagination.PaginationConstants.PAGE_SIZE_DEFAULT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.qsocialnow.common.model.cases.ResultsListView;
import com.qsocialnow.common.model.cases.SubjectFilterRequest;
import com.qsocialnow.common.model.cases.SubjectListView;
import com.qsocialnow.common.model.config.Media;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.common.model.pagination.PageResponse;
import com.qsocialnow.services.SubjectService;

@VariableResolver(DelegatingVariableResolver.class)
public class SubjectsViewModel implements Serializable {

    private static final long serialVersionUID = -759909708888032625L;

    private int pageSize = PAGE_SIZE_DEFAULT;
    private int activePage = ACTIVE_PAGE_DEFAULT;

    @WireVariable
    private SubjectService subjectService;

    private boolean moreResults;

    private List<SubjectListView> subjects = new ArrayList<SubjectListView>();

    private String keyword;

    private boolean filterActive = false;

    @Init
    public void init() {
        findSubjects();
    }

    public List<SubjectListView> getSubjects() {
        return this.subjects;
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

    @Command
    @NotifyChange({ "subjects", "moreResults" })
    public void moreResults() {
        this.activePage++;
        this.findSubjects();
    }

    private PageResponse<SubjectListView> findSubjects() {
        SubjectFilterRequest filterRequest = new SubjectFilterRequest();
        PageRequest pageRequest = new PageRequest(activePage, pageSize, null);
        filterRequest.setPageRequest(pageRequest);
        setFilters(filterRequest);
        PageResponse<SubjectListView> pageResponse = subjectService.findAll(filterRequest);
        if (pageResponse.getItems() != null && !pageResponse.getItems().isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            List<SubjectListView> subjectListViews = mapper.convertValue(pageResponse.getItems(),
                    new TypeReference<List<SubjectListView>>() {
                    });
            this.subjects.addAll(subjectListViews);
            this.moreResults = true;
        } else {
            this.moreResults = false;
        }

        return pageResponse;
    }

    @Command
    @NotifyChange({ "subjects", "moreResults", "filterActive" })
    public void search() {
        this.filterActive = !StringUtils.isEmpty(this.keyword);
        this.setDefaultPage();
        this.subjects.clear();
        this.findSubjects();
    }

    private void setFilters(SubjectFilterRequest filterRequest) {
        if (this.keyword != null && !this.keyword.isEmpty()) {
            filterRequest.setKeyword(this.keyword);
        }
    }

    private void setDefaultPage() {
        this.pageSize = PAGE_SIZE_DEFAULT;
        this.activePage = ACTIVE_PAGE_DEFAULT;
    }

    public boolean isFilterActive() {
        return filterActive;
    }

    @Command
    public void openEdit(@BindingParam("subjectId") String subjectId) {
        Map<String, Object> arg = new HashMap<String, Object>();
        arg.put("subject", subjectId);
        Executions.createComponents("/pages/subject/edit-subject.zul", null, arg);
    }

}